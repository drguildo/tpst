/*
 * Copyright (c) 2016, Simon Morgan
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package io.sjm.tpst;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.PhotoPost;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main {
  private static final String CREDENTIALS_FILENAME = "credentials.txt";

  private static boolean post(JumblrClient client, String blogName, File f) {
    try {
      if (!f.exists()) {
        System.err.println(f + " doesn't exist.");
        System.exit(-1);
      }
      PhotoPost post = client.newPost(blogName, PhotoPost.class);
      post.setData(f);
      post.save();
    } catch (IllegalAccessException | InstantiationException | IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public static void main(String[] args) {
    String blogName, consumerKey, consumerSecret, token, tokenSecret;

    if (args.length < 1) {
      System.err.println("Please specify a URL.");
      System.exit(-1);
    }

    try {
      Scanner credentials = new Scanner(new File(CREDENTIALS_FILENAME));

      blogName = credentials.next().trim();
      consumerKey = credentials.next().trim();
      consumerSecret = credentials.next().trim();
      token = credentials.next().trim();
      tokenSecret = credentials.next().trim();

      System.out.println("Consumer key: " + consumerKey);
      System.out.println("Consumer secret: " + consumerSecret);
      System.out.println("Token: " + token);
      System.out.println("Token secret: " + tokenSecret);

      JumblrClient client = new JumblrClient(consumerKey, consumerSecret);
      client.setToken(token, tokenSecret);

      URL url = new URL(args[0]);
      File destination = new File(url.getFile());

      FileUtils.copyURLToFile(url, destination);
      destination.deleteOnExit();

      System.out.println("Source: " + url);

      System.out.println("Attempting to post " + destination + " to " + blogName + "...");
      boolean success = post(client, blogName, destination);

      if (success)
        System.out.println("Posting complete.");
    } catch (FileNotFoundException e) {
      System.err.println("Please create a credentals.txt of the form:");
      System.err.println("blogName");
      System.err.println("consumerKey consumerSecret");
      System.err.println("token tokenSecret");
      System.exit(-1);
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }
  }
}
