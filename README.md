# tpst

**tpst** is an application for quickly posting images to a Tumblr blog.

Usage: `tpst.jar http://www.example.net/image.jpg`

**tpst** expects a file named `credentials.txt` in the working directory. This file should contain the name of the Tumblr blog you wish to post to, as well as the requisite credentials. The file should be of the form:

```
blogName
consumerKey consumerSecret
token tokenSecret
```
