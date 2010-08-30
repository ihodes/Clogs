# Clogs

Clogs is a simple, Clojure-powered baked blogging engine.

It aims to stay out of the way, and give you fine-grained control over what you want your blog to do, should you want to spend the time making it yours. Clogs uses [Envlive](www.github.com/cgrand/enlive) to format your posts into raw HTML markup, and uses [MarkdownJ](http://markdownj.org/) to format your posts into HTML from Markdown. 

There are no databases involved: everything is static. Every time you publish a post, Clogs prepends the new post to your archives, and re-writes the `index.html` and `feed.xml` files taking the new post into account. This means that when you blog gets hit with a lot of traffic, you aren't relying on your database to keep up the with madness, or any tricky caching schemes (though you can use those, too), as it is just serving static files.

 Metadata can be attached to posts in the form of a Clojure map on the first line of any and every Clogs post. Homoiconic languages rock! Right now, the :title attribute is required, as this gives each post a specific name. You can also specifiy a publish data, if you like; if not, Clogs will put the date in there for you.

Posts aren't plain HTML files: they are each a bundle, in the form of a directory, containing your post in a file called `post.md`, and all resources the post uses (photos, other media, etc) in the same directory. 

Right now, comments aren't supported: I use and recommend Disqus. 

Most documentation is in the source as of now: this will change when it's ready to be used. What is above is a quick sketch of how things work.

## What?
####Is baking? 
See [this post](http://www.aaronsw.com/weblog/000404) on Aaron Swartz's blog, where I (and others) got the name.
####Is with the name? 
Clojure + Blogs = Clogs and it's cute. That is all.

## Clogs Uses
* clojure 1.2.0
* clojure-contrib 1.2.0
* enlive-1.0.0-SNAPSHOT
* clj-time-0.1.0-SNAPSHOT
* markdownj

