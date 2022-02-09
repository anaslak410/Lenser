# Plan :
## Current Issue:
* use ids to refer to nodes and not nums
* make sales main section
* translate project terminology to arabic
## tasks:
* [x] add type to sale class and make it appear
* [x] ability to add sales to bag from diffrent costlists
* [ ] function that converts price to a fitting string representation
* [x] manual button, add it to total and make sure its in bag , might need to edit sale.equals()
* [x] organize project 
* [ ] change size dependency to id , for example in search in gui
* [x] make cell addition increment quantity if the same cell is added
    * [x] update clear bag button 
    * [x] update bag AND tested, see if both have the same value
    * [x] error is at bag get sale 
    * [x] update total since price changes
    * [x] update label for quant
* [x] must be able to remove sales individually
* [ ] mechanism to not allow text
* [ ] fix scroll issue
* [ ] name field
* [ ] make it save fields once one quits
* [x] update total price when quantity is increased manually
* [ ] make bag savable with a button 
* [x] are you sure you want to clear? window when clicked
* [ ] store costlists somewhere safe
* [ ] editable date
* [ ] ribbon with manual loading
* [ ] manual loading must go to a seperate screen to see all available costlists may
* [ ] material design
* [ ] database constraints 
- - - -
## Project Terminology:
    Sale: individual unit sold with quantity.
    cyl: cylinder.
    sphere: sphere.
    Bag: bag of all sales
    costlist: predefined 2d list of costs of lenses
### Conventions :
*
- - - -
## Client Affairs :

### questions:
* how do you want everything positioned?
* do you want quantPrice for each sale? yes
* whata are the words for this and that in arabic
* what should default values be
* clear button to clear name field?
* what should be price limit
* how to load costlists, by searching names or listing available ones
* buyers are specified right? yes
* limitations for DB
* law of no null ?
* should tables be unique
* can you delete buyers? what will happen to bags with the deleted buyers name
* what information do you want about buyers
* what should button names be
* should buyer balance change automatically?
- - - -
## Database design:
### notes:
* names should be unique

