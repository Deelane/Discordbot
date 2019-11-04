# Discordbot
Bot for discord written in java, uses discord4j
Commands:

!roll

  !roll: roll a number between 0 and 100 (0 is intentional)
  
  !roll min, max: roll a number between min and max
  
  
!strawpoll
  
  !strawpoll title, option1, option 2, ..., option 30: creates a strawpoll with the given title and options, up to 30 options. returns the link
  
  
!lolbuild

  !lolbuild champion: returns the default metasrc build for champion
  
  !lolbuild champion, mode: returns the metasrc build for the champion and mode given
  
  !lolbuild champion, role: returns the metasrc build for the champion and role given (summoner's rift)
 
 
 Features:
 
 RedditImagePoster: Creates a list of the top 5 posts from every subreddit in properties file. Shuffles the list and posts 1 post every 5 minutes to discord channel.
