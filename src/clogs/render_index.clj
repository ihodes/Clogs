(ns clogs.render-index
  (:require [net.cgrand.enlive-html :as html])
  (:use compojure.core
        [clojure.contrib.duck-streams :only (spit)]))

;; defines the html to be used as a template
;; loads this from settings.clogs later
(def *index-template-file* "resources/template_index.html")

(defn render
  "Little helper for taking a list of strings and making a string from it."
  [xs]
  (apply str xs))

;; models a post: needs to add permalink portion later
;; and needs to handle images, later
(html/defsnippet post-model *index-template-file* [:.cg-post]
  [{:keys [title author pubdate post-text]}]
  [:.cg-post-title] (html/content title)
  [:.cg-post-author] (html/content author)
  [:.cg-post-pubdate] (html/content pubdate)
  [:.cg-post-text] (html/html-content post-text))

(html/deftemplate render-index *index-template-file*
  [{:keys [posts]}]
  [:.cg-wrap] (html/clone-for [post posts]
                              (html/content (post-model post))))


(def *fpost*
     {:title "A post!"
      :author "Isaac Hodes"
      :pubdate "Today"
      :post-text "There's some text here, but not much"})

(def *fake-data*
     {:posts [{:title "Wow, here's a post"
               :author "Isaac Hodes"
               :pubdate "May 10th, 2010"
               :post-text "<p>My sophomore year at Carleton is coming to an end, and my summer is soon to begin.</p>

<p>Unfortunately, my summer really started early this week when temperatures hit 75º at night, and sleeping was a cruel impossibility. So instead of sleeping, I began to learn Clojure. I’d begun to learn this amazing language a few months ago, and quickly read through the majority of Stuart Halloway’s Programming Clojure, but I never really got around to writing any real code until earlier this week.</p>

<p>Since then I’ve gone through some of the Project Euler problems, some little explorations of my own, and have begun to make headway in my first serious web-app: the backend of my new blog. I’m using mainly Enlive to write the blog (so far), and intend to make it a “baked” blog, both to improve the speed of the blog and the reduce both the complexity and resources required to set up a blog like it.</p>

<p>I’ll be posting updates here as things move forward, and posting some potential designs rather soon, but in the meantime, I’ll struggle for sleep as both the excitement of programming in Clojure and the stifling heat of Minnesota keep me awake.</p>"}
              {:title "Here's another one!"
               :author "Isaac Hodes"
               :pubdate "May 10th, 2010"
               :post-text "<p>The one bank I always hear spoken positively of is USAA, and sometimes ING Direct, and with even some level of passion. But no one really loves their bank. No one has any real attachments to their bank other than the artificial and unnecessary strings banks keep you using their services with.</p>

<p>BankSimple is a new bank, and its goal is to be simple. Founded by Josh Reich, Shamir Karkal, and recently, Alex Payne, this bank looks promising for a few reasons, including the fact that over the course of a week, Josh and Shamir sent out hundreds of emails to people who had signed up to be involved in the bank’s beta and personally responded to all who replied. While this is certainly not a scalable and sustainable model with just a few employees, I already know how much they care about making their bank work for their customers.</p>

<p>Check out On Banking Reform on their blog for some insight into what they want to do, and to read more about reasons banking, right now, is broken.</p>

<p>From my comment on HN,</p>

<p>That’s why I’ll be using it first, and lots of my fellow college students who have a lot less to use and a lot more to gain from using a bank like BankSimple.Time is in short supply, and fees eat away at my little pool of cash.</p>

<p>I’m willing to take the risk, and willing to put my money in the hands of people who are young, smart and innovative, instead of bureaucratic, slow and set in their ways.</p>

<p>…</p>

<p>Finally, the way things are going now, the government is the only reason those “grave old men in suits” still have your money at all.﻿</p>

<p>I can’t wait to start using BankSimple.</p>"}
              {:title "Blah blah blah another test here"
               :author "Isaac Hodes"
               :pubdate "May 10th, 2010"
               :post-text "<p>Appropriately, I am writing this as I lie in bed trying to cajole myself to sleep. I’m utterly failing; it is difficult to talk yourself into doing something you have no desire to do.</p>

<p>When I sleep, it is a concession to the inconvenient demands of my imperfect body. It’s an implicit surrender to an ancient and inefficient imperative: energy conservation and consolidation of memories, as well as some other shenanigans we as the human race aren’t too sure of yet.</p>

<p>The problem is that there is too much going on. I always have something I either need to do, desperately want to do, or am currently doing. This may range from writing to solving math problems, programming to hiking in the arboretum, even just strolling through campus and thinking about X or doing some anaerobic exercise in the gym. I concede: sometimes the doing is as unimportant as playing a video game or watching a TV show. But I always want to be doing.</p>

<p>You wouldn’t fall asleep during a riveting movie, would you? That’s how I feel about every day. I sleep when I am too tired to keep functioning at a level that would make it worth staying awake.</p>

<p>When I do sleep, I relish every rare dream and every nightmare: at least then I feel alive.</p>

<p>There is too little time in the world to do a tenth of what I plan to do. Why trade a second of that for something so close to death as sleep?</p>"}
              {:title "Cantor sets and shiz"
               :author "Isaac Hodes"
               :pubdate "May 10th, 2010"
               :post-text "<p>There is something about coffee shops and cafés (what’s the different between the two, anyway?) that makes them especially conducive to bouts of extreme productivity. I go to one, read for an hour, write a few thousand words for myself and for a newspaper article, work on a bit of math, program for another hour, and then head home.</p>

<p>If only I could keep that up for more than a few hours. After a while, the caffeine and an empty stomach begin to work together in particularly unpleasant ways, and the gentle hum of people in the coffee shop begins to grate. So I leave.</p>

<p>It’s a treat I enjoy in moderation, but what a treat it is.</p>

<p>•••</p>

<p>As a side note, today I began to learn about Django, a web development framework I’ll need for an idea I’ll begin working on later this week. More information as that begins to come together. I had used Rails in the past, but decided I’d rather stick with Python (a language I’m familiar with) for this project. I’d just like to get something out there and useful at this point, rather than struggle with both a framework and an unfamiliar language like Ruby, beautiful though ﻿it may be.</p>"}
              {:title "The last test title"
               :author "Isaac Hodes"
               :pubdate "May 10th, 2010"
               :post-text "<p>At some point tomorrow, I’ll have deleted my Facebook account (or have tried my best to–I hear it’s difficult.)</p>

<p>I’m certainly not the first, and there will be many more. Here’s why:</p>

<p>According to the EFF, and my memory, Facebook’s privacy policy used to look like this in 2006:</p>

<p>We understand you may not want everyone in the world to have the information you share on Facebook; that is why we give you control of your information. Our default privacy settings limit the information displayed in your profile to your school, your specified local area, and other reasonable community limitations that we tell you about.﻿</p>

<p>In 2005, it looked like this:</p>

<p>No personal information that you submit to Thefacebook will be available to any user of the Web Site who does not belong to at least one of the groups specified by you in your privacy settings.﻿</p>

<p>Now in 2010, after Facebook’s F8 conference in April, the policy looks more like this:</p>

<p>When you connect with an application or website it will have access to General Information about you. The term General Information includes your and your friends’ names, profile pictures, gender, user IDs, connections, and any content shared using the Everyone privacy setting. …The default privacy setting for certain types of information you post on Facebook is set to “everyone.”</p>

<p>And, as I said on HN* over a week ago, I’m not okay with it. The majority of Facebook’s users might be, and the majority of Facebook’s users may stay, but I need to leave. Aside from this timely compilation of Reasons [to] Quit Facebook, there is another reason that I need to quit.</p>

<p>Facebook has disrespected its users. When I joined years ago, I agreed to a Privacy Policy which set my expectations their treatment of me and my data. I did not think I’m obligated to the services they offered, and I was appreciative of what they were giving me. I was even okay with my basic information being anonymously scraped to generate ads that were relatively unobtrusive.</p>

<p>It’s not that I need to hide things, or that I am worried they will eventually leak some data about me I’d rather they not have. But it’s their blatant disregard for their users that stings the most. It’s disrespectful to treat our data so poorly that you could watch in on your friends’ private chats or see the IP addresses of users who interact with you. Some apps were surreptitiously added to profiles of users visiting websites like the Washington Post, CNET, formspring.m.</p>

<p>Everyone makes mistakes, and these were not catastrophic mistakes as far as I know. But they could have been harmful for some of their users, and no doubt some of their about 300 millions users were adversely affected by these mistakes. After every one of these frequent incidents there should have been a very public apology. There was not.</p>

<p>I’ve heard many people say something to the effect of “you shouldn’t put on the internet what you don’t want other’s to see.” Being charitable and assuming they don’t mean anything at all, which would leave me to believe they do not have bank accounts, businesses or Amazon accounts with private financial and very personal information stored in 3rd party databases, I still think these people are mistaken for saying that it’s no big deal.</p>

<p>If 37signals leaked your business’s data or your startup’s plans to the public, there would be an outcry. If there was no reason to hide anything posted online, there wouldn’t be companies like Matasano Security nor would there be much reason to have passwords except to differentiate accounts on a particular service.</p>

<p>The internet is a complex and tangled place, and a dedicated cracker can find just about anything that is connected somehow to the web. But that is as true about meastspace as it is about the internet, and it’s no reason to stop locking money in safes or protecting nuclear launch codes.</p>

<p>I know, I know. The pictures of my most recent meal and the protection of WMDs are hardly equivalent in importance. But that doesn’t mean I want Facebook to jerk me around, and treat my data like the mere advertising fodder it is to them. At least the pretense that I am a valued client of theirs should be kept up. Facebook should NOT change my privacy settings from completely private to completely open to everyone without at least asking me, or even telling me and giving me some warning. U.S. Senator Al Franken (MN) himself is a bit upset.</p>

<p>So I am leaving Facebook, not just because they treat my data poorly, but because they treat me poorly. There will be an alternative eventually, and chances are that I’ll try it out. There’s room in my life for something like Facebook, but for now my email, Twitter, my blog and my phone will keep me close enough to everyone.</p>

<p>* I meant to leave sooner, but were just so many juicy stories to post on Facebook about Facebook after F8.</p>"}]})  