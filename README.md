Simple-Music-Player
===================

The music apps I've found for Android have either been buggy or have had bad interfaces, so my first attempt at an app
is making a music player that I like. 

This project started from an example at http://www.androidhive.info/.

Version 0.9.1:

Added some framework for a playlist system. There's are now classes for songs/albums/artists, and there's a tab-swipe UI for selection. Loading the fragments into that framework is not yet working.

Minor fixes: 

 - Program no longer crashes on exit.

 - Previous button has been changed to return to the beginning of the song  if the progress is past three seconds. This is more in line with expected behavior.



Version 0.9:

More general UI improvements, and the ability to change the directory which is searched for mp3 files via the settings menu. The remaining tasks: 

 - setting up a playlist / queuing system
 
 - changing the directory picker to a GUI rather than having it text-based
 
 - implementing the player as a service, to have it show up in the notifications tray
 
As is, it's pretty useable, at least if you like listen to the same album over and over like I do.



Version 0.8.1:

A few things have been fixed. The player looks better overall, with some color and icon changes, and the slider has been switched to one I like more. Album art retrieval is now working correctly.

Repeat and shuffle are working, but only if songs terminate naturally, i.e. not with previous and next buttons. This is 
not worth independently fixing, as it should be fixed as part of a more robust queue / playlist structure I'm planning.

Another goal is to add a presence to the notification pane which allows the user to pause / next / previous during playback.

The next task is definitely distinguishing between a current playlist and the entire library, which should require some 
very serious structural shifts.



Version 0.8:
The player is mostly working, but with the following issues:

 - Repeat is not working.
      I'm not sure what's causing this.

 - Buttons for shuffle and repeat need to look pushed down when activated.
      This is just cosmetic.

 - Album art does not change with the song.
      This probably requires copying some code to a different class.

 - The player only loads music from the directory /sdcard/media/audio/mp3.
      This is the big issue. It needs to, at the minimum, also scan subdirectories. Eventually the user should be 
      able to specify directories to scan.
      
I think once these issues are addressed it will have earned the version 1.0.
