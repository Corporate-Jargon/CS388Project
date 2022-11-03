# APP NAME HERE

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
1. [Schema](#Schema)

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

* Newly registered users are redirected to the create/join community page
* User logs in and is redirected to their primary community's page
* Members of a community can create/view/edit posts
* Relative time of post is shown
* Once a week, communities with different interests are combined (into a third community), with the goal of members sharing their interests with the other community.
* After a certain time period, the two communities return back to normal and the process repeats.
* Users can toggle their participation in weekly community combinations.
* Profile pages for each user
* Settings (Accesibility, Notification, General, etc.)

**Optional Nice-to-have Stories**

* Members of a community can vote to remove a member
* Posts can be flagged for removal
* Following other users
* Can search for communities
* Users can own/create more than one community
* Users can select moderators for the community

### 2. Screen Archetypes

* Login 
* Register - User signs up or logs into their account
   * New users are redirected to the join/create community page
   * Existing users are redirected to their primary community feed after login
* Join/Create Community
   * Users can either join or create a new community if it does not exist. This community becomes their primary community
* Profile Screen 
   * Allows user to upload a photo and fill in required information
   * Allows user to select their primary community
* Community feed
   * Allows user to view posts from other memebers of the community
   * Allows user to edit their own posts
* Create Post
    * Allows user to create post
* Settings Screen
   * Lets people change app notification settings.

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
<img src="https://i.imgur.com/9CrjH1K.jpg" width=800><br>

### [BONUS] Digital Wireframes & Mockups
<img src="https://i.imgur.com/lYHn37F.jpg" height=200>

### [BONUS] Interactive Prototype
<img src="https://i.imgur.com/AiKfE5g.gif" width=200>

## Schema 
### Models
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User| image author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | likesCount    | Number   | number of likes for the post |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
### Networking
#### List of network requests by screen
   - Home Feed Screen
      - (Read/GET) Query all posts where user is author
         ```swift
         let query = PFQuery(className:"Post")
         query.whereKey("author", equalTo: currentUser)
         query.order(byDescending: "createdAt")
         query.findObjectsInBackground { (posts: [PFObject]?, error: Error?) in
            if let error = error { 
               print(error.localizedDescription)
            } else if let posts = posts {
               print("Successfully retrieved \(posts.count) posts.")
           // TODO: Do something with posts...
            }
         }
         ```
      - (Create/POST) Create a new like on a post
      - (Delete) Delete existing like
      - (Create/POST) Create a new comment on a post
      - (Delete) Delete existing comment
   - Create Post Screen
      - (Create/POST) Create a new post object
   - Profile Screen
      - (Read/GET) Query logged in user object
      - (Update/PUT) Update user profile image
#### [OPTIONAL:] Existing API Endpoints
##### An API Of Ice And Fire
- Base URL - [http://www.anapioficeandfire.com/api](http://www.anapioficeandfire.com/api)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /characters | get all characters
    `GET`    | /characters/?name=name | return specific character by name
    `GET`    | /houses   | get all houses
    `GET`    | /houses/?name=name | return specific house by name

##### Game of Thrones API
- Base URL - [https://api.got.show/api](https://api.got.show/api)

   HTTP Verb | Endpoint | Description
   ----------|----------|------------
    `GET`    | /cities | gets all cities
    `GET`    | /cities/byId/:id | gets specific city by :id
    `GET`    | /continents | gets all continents
    `GET`    | /continents/byId/:id | gets specific continent by :id
    `GET`    | /regions | gets all regions
    `GET`    | /regions/byId/:id | gets specific region by :id
    `GET`    | /characters/paths/:name | gets a character's path with a given name
