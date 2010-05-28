# Clogs

Clogs is a simple, Clojure-powered baked blogging engine.

Right now, it can parse a posts.xml file and render an index from a template.

Most documentation is in the source.

## Plans

- ClogsDaemon: launches the baker when a new posts is uploaded.
- Baker: Checks to see if the post it's handed is newer than the 
         newest post in the posts.xml file. Also should populate
          an archives page.
- Clogger: Handles an AtomFeed (and RSS).
- CandlestickMaker: Handles comments in a oven-friendly manner.

## What?
Is baking? http://www.aaronsw.com/weblog/000404
Is with the name? Clojure + Blogs = Clogs and it's cute. That is all.

## Uses
clojure 1.1.0
clojure-contrib 1.1.0
enlive-1.0.0-SNAPSHOT
clj-time-0.1.0-SNAPSHOT

## License
© Isaac Hodes 2010
www.copperthoughts.com
