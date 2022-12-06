# MXER

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Schema](#Schema)
1. [Sprint 1](#Sprint-1)

## Overview
### Description
Community-based social media app. Could be used to meet new people with similar interests or to meet people from within other communities.

### App Evaluation
- **Category:** Social Networking
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer, such as tinder or other similar apps. Functionality wouldn't be limited to mobile devices, however mobile version could potentially have more features.
- **Story:** Groups people based on their interests. Two communities are then temporarily combined through the use of "events" to create one large community of unlike interests. Users can decide if they wish to participate in the combining of groups.
- **Market:** Any individual could choose to use this app, and to keep it a safe environment, people would be organized into communities based on user selection..
- **Habit:** This app could be used as often or unoften as the user wanted depending on how deep their social life is, and what exactly they're looking for.
- **Scope:** First we would connect communities based on unlike interests. This then could evolve into a competition based app where users can try to create the best post within their communities.

## Product Spec
### 1. User Stories (Required and Optional)

**Required Must-have Stories**

- [X] Newly registered users are redirected to the create/join community page
- [X] User logs in and is redirected to their primary community's page
- [x] Members of a community can create/view/edit posts
- [x] Relative time of post is shown
- [X] Communities with different interests can be combined into a third community, with the goal of members sharing their interests with the other community.
- [X] After a certain time period, the two communities return back to normal and the process repeats.
- [X] Users can toggle their participation in weekly community combinations.
- [x] Profile pages for each user
- [x] Settings (Accesibility, Notification, General, etc.)
- [x] Toggleable Profanity Filter, requires due consideration
- [x] User can update profile picture
- [x] AI API for Moderation

**Optional Nice-to-have Stories**

* Members of a community can vote to remove a member
* Posts can be flagged for removal
* Following other users
* Can search for communities
* Users can create a community
* Users can select moderators for the community

### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   - New users are redirected to the join/create community page
   - Existing users are redirected to their primary community feed after login
* Join/Create Community
   - Users can either join or create a new community if it does not exist. This community becomes their primary community
* Profile Screen 
   - Allows user to upload a photo and fill in required information
   - Allows user to select their primary community
   - Settings
* Community feed
   - Allows user to view posts from other memebers of the community
   - Allows user to edit their own posts
   - Browse Communities
* Detail View
   - Can comment on posts and reply to other comments
* Create Post
    - Allows user to create post
* Settings Screen
    - Lets people change app notification settings.
* Events (Mixes)
    - Allows users to see list of events (mixes) involving their communities.
    - Owner of community can create or close events (mixes).

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Thread
* Events
* Discover
* Profile
* Settings

Optional:
* 

**Flow Navigation** (Screen to Screen)
* Login/Register -> Thread/Events/Discover/Profile/Settings
* Thread -> Thread of your primary community -> View/Edit/Make posts
* Events -> view current events
* Discover -> View other communities/users and their threads
* Profile -> View and edit profile 
* Settings -> Toggle settings

## Wireframes
<img src="https://raw.githubusercontent.com/Corporate-Jargon/Mxer/master/wireframes/Lo-Fi.png" width=800><br>

### [BONUS] Digital Wireframes & Mockups
<img src="https://raw.githubusercontent.com/Corporate-Jargon/Mxer/master/wireframes/Hi-Fi.png" height=200>

### [BONUS] Interactive Prototype
<img src="https://raw.githubusercontent.com/Corporate-Jargon/Mxer/master/wireframes/Prototype.gif" width=200>

## Schema 
### Models
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| post author |
   | image         | File     | image that user posts |
   | description     | String   | post text |
   | community | Pointer to Community | community which post was made in
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
#### User
| Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for user (default field) |
   | name       | String| Display name of user |
   | profile_picture        | File     | Profile Picture of user |
   | description     | String   | Editable "About Me" for user |
   | primary_community | Pointer to Community | Primary Community of User |
   | joined_communities | Array of Pointer to Community | All communities which this user is a member of |
   | createdAt     | DateTime | date when user is created (default field) |
   | updatedAt     | DateTime | date when user is last updated (default field) |
#### Community
 | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for community (default field) |
   | name       | String| Display name of community |
   | icon        | File     | Icon for community |
   | description     | String   | Description of community |
   | event_community1 | Pointer to Community | If specific community is event, points to first community in the event. null otherwise
   | event_community2 | Pointer to Community | If specific community is event, points to second community in the event. null otherwise
   | createdAt     | DateTime | date when community is created (default field) |
   | updatedAt     | DateTime | date when community is last updated (default field) |
 #### Comment
  | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the comment (default field) |
   | author        | Pointer to User| comment author |
   | reply_to | Pointer to Post | Post which comment is a comment of
   | description     | String   | comment text |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
### Networking
#### List of network requests by screen  
   - Create Post Screen
      - (Create/POST) Create a new post object
      ```kotlin
        fun makePost(image: File, description: String, community: Community){
            val post = Post()
            post.setAuthor(ParseUser.getCurrentUser())
            post.setImage(ParseFile(image))
            post.setDescription(description)
            post.setCommunity(community)
            post.saveInBackground { e ->
                if (e != null) {
                    Log.e(TAG, e.printStackTrace().toString())
                } else {
                    Log.i(TAG, "Successfully saved post")
                }
            }
        }
      ```
  - Post Detail Screen
      - (Create/POST) Create a new reply object
      ```kotlin
        fun makeReply(post: Post, description: String){
            //make comment reply
            val comment = Comment()
            comment.setAuthor(ParseUser.getCurrentUser())
            comment.setReplyTo(post)
            comment.setDescription(description)
            comment.saveInBackground { e ->
                if (e == null) {
                    Log.i(Companion.TAG, "Successfully replied")
                } else {
                    Log.e(Companion.TAG, e.printStackTrace().toString())
                }
            }
        }
      ```
      - (Read/GET) Query all replies to post
      ```kotlin
        fun getReplies(post: Post): List<Post> {
             val query = ParseQuery<ParseObject>("Comment")
             query.addDescendingOrder("createdAt")
             query.whereEqualTo("reply_to", currentPost)
              query.findInBackground { objects: List<Post>, e: ParseException? ->
                  if (e == null) {
                      Log.d(Companion.TAG, "Objects: $objects")
                      return objects
                  } else {
                      Log.e(Companion.TAG, "ParseError: ", e)
                  }
              }
        }
    ```
  - Community Screen
      - (Read/GET) Query all posts from community
         ```kotlin
        fun getPosts(community: Community): List<Post> {
         val query = ParseQuery<ParseObject>("Post")
         query.addDescendingOrder("createdAt")
         query.whereEqualTo("community", currentCommunity)
          query.findInBackground { objects: List<Post>, e: ParseException? ->
              if (e == null) {
                  Log.d(Companion.TAG, "Objects: $objects")
                  return objects
              } else {
                  Log.e(Companion.TAG, "ParseError: ", e)
              }
            }
        }
         ```
    - (Update/PUT) Allow user to join community
    ```kotlin
    fun joinCommunity(community: Community){
        val user = ParseUser.getCurrentUser()
        val comms = user.getJoinedCommunities()
        comms.append(community)
        user.setJoinedCommunity(comms)
        user.saveInBackground { e ->
             if (e == null) {
                 Log.i(Companion.TAG, "Successfully saved profile picture")
             } else {
                 Log.e(Companion.TAG, e.printStackTrace().toString())
             }
        }
    }
    ```
   - Profile Screen
      - (Read/GET) Query logged in user object
      ```kotlin
         val query = ParseQuery<ParseObject>("User")
         query.whereEqualTo("objectId", currentUser)
          query.findInBackground { objects: List<ParseObject>, e: ParseException? ->
              if (e == null) {
                  Log.d(Companion.TAG, "Objects: $objects")
              } else {
                  Log.e(Companion.TAG, "ParseError: ", e)
              }
          }
        ```
      - (Update/PUT) Update user profile image
      ```kotlin
        fun setPfp(image: File){
            val user = ParseUser.getCurrentUser()
            val pFile = ParseFile(image)
            user.put("profile_picture", pFile)
            user.saveInBackground { e ->
                if (e == null) {
                    Log.i(Companion.TAG, "Successfully saved profile picture")
                } else {
                    Log.e(Companion.TAG, e.printStackTrace().toString())
                }
            }
        }
        ```
  - Events Screen
      - (Read/GET) Query events 
      ```kotlin
        fun getEvents(): List<Community>{
         val query = ParseQuery<ParseObject>("Community")
         query.whereExists("event_community1")
          query.findInBackground { objects: List<Community>, e: ParseException? ->
              if (e == null) {
                  Log.d(Companion.TAG, "Objects: $objects")
                  return objects
              } else {
                  Log.e(Companion.TAG, "ParseError: ", e)
              }
           }
        }
    ```
 - Communities Screen
     -  (Read/GET) Query communities
      ```kotlin
        fun getCommunities(): List<Community> {
         val query = ParseQuery<ParseObject>("Community")
          query.findInBackground { objects: List<Community>, e: ParseException? ->
              if (e == null) {
                  Log.d(Companion.TAG, "Objects: $objects")
                  return objects
              } else {
                  Log.e(Companion.TAG, "ParseError: ", e)
              }
          }
        }
    ```
#### Existing API Endpoints
##### Perspective API
- Base URL - [https://commentanalyzer.googleapis.com/v1alpha1](https://commentanalyzer.googleapis.com/v1alpha1)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `POST`    | /comments:analyze | get the toxicity probability
#### Sprint 1
<img src="https://github.com/Corporate-Jargon/Mxer/blob/master/Peek%202022-11-21%2019-41.gif"></img>
- [X] Newly registered users are redirected to the create/join community page.
- [X] User logs in and is redirected to their primary community's page
- [X] Design tables in Parse
- [X] Design Login and Register Pages
