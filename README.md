# Guess The Celebrity - Android Studio App 
The cool thing about this app is that it uses an external webpage to download the content that will make the app work. Right when the app starts, in a background task, it will read the .html file of this webpage https://www.imdb.com/list/ls052283250/, stores it in a String, and then using a Regex it filters and get the names and the image links of the celebrities and stores them in two separate ArrayLists. From this point on it's fairly simple to put everything together and make it work.

# Demo
![](https://i.imgur.com/LKpgY3A.gif)
  
minSdkVersion 15  
targetSdkVersion 29  
