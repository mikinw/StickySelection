StickyHighlight
===============

IntelliJ Idea plugin with which you can permanently highlight any selection. Similar to the Notepad++ feature, where you can "paint selection".

      You can define an arbitrary number of Paint Groups. Selecting the appropriate editor action
      (keystroke or context menu), the all occurrences of currently selected text will be added to
      the Paint Group and will be permanently highlighted (until you clear the selection with an
      other editor action). So you can have different text fragments to be selected with the same
      Paint Group.

      * You can set different colours for each Paint Group
      * You can set a marker to be visible on the right side of the editor
      * You can add multiple selections to the same group
      * You can convert a Paint Group to multi caret selection (and thus edit, copy, delete, etc. it)
      * For convenience you can undo the last addition (until the document is edited)
      * You can cycle through each element in a given Paint Group or in all Paint Groups
      * Keymap actions are added dynamically for paint, clear and convert as you add more Paint Group

      Undo works up until the document is changed.
      Navigating in a specific Paint Group works like this:
      * If the caret is inside a Paint Group upcoming navigation will move caret among the Paints in that Paint Group.
      * If the caret is not inside a Paint Group the plugin will find the closest selection (in the direction the
      navigation happens), and will select that Paint Group for upcoming navigation
      * In the setting you can select if you wish the navigation to continue from the beginning of the document
      when you have reached the end of the document (or from the end if you have reached the beginning, when navigating
      to the other direction)
      * If caret is moved, decision starts from the beginning.


      Default shortcuts (but I highly encourage to define your own or at least check with your current
      shortcuts)>

      * Press *Ctrl + Alt + F9* to paint a selection (showing popup for selecting Paint Group). If
      nothing is selected, then the plugin tries to select automatically the word or block under the caret.
      * Press *Ctrl + Alt + F10* than *F9* to clear a selection (showing popup for selecting Paint Group).
      * Press *Ctrl + Alt + F10* than *F10* to clear all selections.
      * Press *Ctrl + Alt + F8* to undo last selection.
      * Press *Ctrl + Alt + quote* to navigate to next Paint in the current Paint Group.
      * Press *Ctrl + Alt + semicolon* to navigate to previous Paint in the current Paint Group.

      <br>Note: control + alt on Mac is (I guess) meta + alt
