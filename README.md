# Zulip API based chat app

- Getting all data from Zulip API
- Caching lists of streams, topics, contacts and messages
- Authorization with your own API KEY
- UI test coverage of the message list and reaction actions
- Unit test coverage of the message repository and entities responsible for the business logic of adding/removing reactions

# Screens

### Channels screen

<img src="/readme/Channels_screen.jpg" width="200" align="left" hspace="10">

Features:
- Channel search
- Own channels creation
- Clicking on a channel opens a list of channel topics
- Clicking on a topic opens the topic chat

<br clear="left"/>

### Chat screen

<img src="/readme/Chat_screen.jpg" width="200" align="left" hspace="10">

Features:
- Sending and receiving messages
- Sending and receiving attachments
- Custom View for displaying Emoji reactions and number of reactions
- Custom ViewGroup that displays the message along with the reactions
- Pagination

<br clear="left"/>

### Message actions

<img src="/readme/Chat_actions.jpg" width="200" align="left" hspace="10">

On long click, bottom sheet dialog opens, where you can:
- Add reaction
- Copy message to the clipboard
- Edit message
- Change topic of this message
- Delete message

<br clear="left"/>

### People screen

<img src="/readme/People_screen.jpg" width="200" align="left" hspace="10">

User's contact list. Features:
- Displaying the status of users
- User search

<br clear="left"/>

### Profile screen

<img src="/readme/Profile_screen.jpg" width="200" align="left" hspace="10">

Just user profile

<br clear="left"/>
