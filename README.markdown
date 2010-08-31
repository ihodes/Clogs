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

## To Use 
####Preliminary (this is an alpha version, after all)
You'll want to make a directory to hold all of your posts (I use `p/`) and put it in the `resource/` directory. 

Now you can make your first post. It's simple: create another directory, this time with the name you want to be the post's URL. Place that directory inside the `p/` directory, and create a `post.md` file. On the first line of that file, you should create a map: this will be used to supply the metadata for the post. Right now, you should at *least* supply a `:title`, which will be used as, you guessed it, the title of the post, and `:summary` that is used on the archives page. 

e.g. `{:title "How to do this" :summary "Wherein I explain how this is done."}`

If you want, you can add your own "published" date: 

   :date "2010-08-22"

Note that it's in the form "YYYY-MM-DD", which is required.

Under that, you may start writing your post, in the Markdown syntax.

When you are ready to publish, the process for the first post is a little different. 

Where postdir = "resources/p/your-post-dir/", run these commands in the `core` namespace:

* (pre-publish-post postdir)
* (build-index)
* (build-rss)
* (build-archives)

And you should be set. 

For subsequent posts, you should run Clogs and, in the `core` namespace, run (publish-post "resources/p/post-dir/"). You should then be good to go. 