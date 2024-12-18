# CS50 Final Project - "Please Be You" Flash Cards

Name: Daryl Wong<br/> 
Final Project Title: "Please Be You" Flash Cards<br/>
Programming Language: Java

For my final project, I made an Android application inspired by a children's book written by my colleague which is 
an educational ABC book that uses words associated with important values and concepts of self-love to teach kids
the alphabet.  For example, instead of words like "cat" or "elephant," this book uses words like "confident" or 
"empathy" to teach kids their ABCs.  The Android app I built takes the concepts from this book and presents them as 
virtual flashards for users to browse through.  

Upon launching the app, the user is presented with a splash screen and main menu along the bottom.  In the menu, 
selecting "About" will take users to a screen sharing more information about the Please Be You vision and selecting
Settings will allow a user to choose if they want to see the flashcards in random or alphabetical order.

Selecting "Start Flashcards" on the initial splash screen will load flashcards for users to browse through.  These
flashcards use two Java fragments.  The first is for the front of the flashcard which displays a letter of the alphabet
and the second is for the back of the flashcard which displays the corresponding word, a short description, and an
illustration.  The copy and illustrations come directly from the book and are loaded into the application via a headless 
CMS known as Contentful, where the content is uploaded and managed.  Lastly, two floating action buttons on the flashcards
enable users to flip through the different flashcards.
