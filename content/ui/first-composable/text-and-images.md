---
id: ui/first-composable/text-and-images
title: "How do I put words and pictures on the screen?"
status: shadow
difficulty: easy
order: 3
requires: [ui/first-composable/what-is-a-composable]
summary: Text puts words on the screen and Image puts a picture there, with a description for people who cannot see it.
video: ""
publishedOn:
updatedOn:
sims: [postcard-words-and-photo]
playground: false
fruit: false
upnext: false
---

<!--
SOURCES (housekeeper strips this comment at go-live)
- https://developer.android.com/develop/ui/compose/text/display-text (read 21 Oct 2026): the most basic way to display text is the Text composable with a String, Text("Hello World"); "We recommend you use string resources instead of hardcoding Text values, as you can share the same strings with your Android Views as well as preparing your app for internationalization"; Text(stringResource(R.string.hello_world)).
- https://developer.android.com/develop/ui/compose/graphics/images/loading (read 21 Oct 2026): Image displays a graphic; painterResource loads an image (PNG, JPEG, WEBP) or vector resource from the disk; Image(painter = painterResource(id = R.drawable.dog), contentDescription = stringResource(id = R.string.dog_content_description)); supply a contentDescription for visual elements, TalkBack reads it out, the text must be meaningful if read out loud and translated; for a purely decorative element set contentDescription to null so the screen reader ignores it; to load from the internet use a third-party library such as Coil, whose AsyncImage(model = url, contentDescription = ...) handles caching and networking.
- Not checked on an official page, labelled "general knowledge" in the lesson: that a resource lives in the res folder and that R is a class made by the build; that an Image shows without a size.
- The exact signature of Image (which parameters have defaults) was not read in this run, so the lesson does not claim anything about it.
- Compose code needs an Android project, so every code block is read-only or paper (docs/06 section 1).
-->

## The question

You are building a screen that shows a short greeting and a photo of a dog. What do you write to put each one there? Does the photo need a size before it shows at all? And what should happen for a person who cannot see the photo?

## Mental model

Think of writing on a postcard and sticking a photo next to it.

- The postcard is the screen.
- The words are the message you write.
- The photo is the picture you stick on.
- The phrase book is a notebook where all your sentences are kept under names, so you can look one up by name and swap the whole book for another language.
- The photo box holds your pictures, each with a name.
- The caption is a short note under the photo that says what it shows, for someone who cannot see it.

You ask for the words, you ask for the photo, and you write a caption. You do not draw letters or measure the photo first.

**Where the analogy stops:** A real postcard has one language and a fixed size. An app can show a different language from the phrase book, and screens come in many sizes. Also, a real photo cannot speak. On a phone, a screen reader reads the caption aloud, and a picture with no caption leaves that person with nothing.

## See it move

```sim
id: postcard-words-and-photo
template: compare
title: "A postcard with words and a photo"
intro: "A postcard has two parts to fill. On the left, words go in with Text. On the right, a photo goes in with Image. Nothing is on the postcard yet."
sides:
  - id: words
    label: "Words: a Text"
    boxes:
      - { id: source, label: "Where the words come from: not chosen" }
      - { id: card, label: "Postcard: empty" }
  - id: photo
    label: "Photo: an Image"
    boxes:
      - { id: source, label: "Where the photo comes from: not chosen" }
      - { id: card, label: "Postcard: empty" }
      - { id: reader, label: "Screen reader says: nothing yet" }
actions:
  - id: show-text
    label: "Show the words"
    do:
      - label:
          words.source: "Where the words come from: a quoted string in the code"
          words.card: "Postcard: shows the words"
      - light: [words.card]
      - say: "Text takes the words you give it, and the postcard shows them."
  - id: words-resource
    label: "Take the words from a string resource"
    do:
      - label:
          words.source: "Where the words come from: a named entry in the phrase book"
      - light: [words.source]
      - say: "The postcard shows the same words, but now they live in one named place that a translation can replace."
  - id: show-photo
    label: "Show a photo from a picture file"
    do:
      - label:
          photo.source: "Where the photo comes from: a picture file in the photo box"
          photo.card: "Postcard: shows the photo"
      - light: [photo.card]
      - say: "Image takes a picture and draws it. No size was asked for."
  - id: add-description
    label: "Give the photo a description"
    do:
      - label:
          photo.reader: "Screen reader says: the description"
      - light: [photo.reader]
      - say: "A screen reader reads the content description aloud to someone who cannot see the photo."
  - id: decoration
    label: "Mark the photo as only decoration"
    do:
      - label:
          photo.reader: "Screen reader says: skips the photo"
      - light: [photo.reader]
      - say: "With the description set to null, the screen reader ignores a picture that is only decoration."
script: [show-text, words-resource, show-photo, add-description, decoration]
try:
  - text: 'Press "Show the words", then "Take the words from a string resource".'
    done: [show-text, words-resource]
  - text: 'Press "Show a photo from a picture file". The postcard shows it, and no size was asked for.'
    done: [show-photo]
  - text: 'Press "Give the photo a description" and read what the screen reader says.'
    done: [add-description]
  - text: 'Press "Mark the photo as only decoration" and read what the screen reader says now.'
    done: [decoration]
    optional: true
```

**What you should notice:** Words and a photo each have their own composable, the words can come from a named place, and the photo gets a description unless it is only decoration.

## Build the logic

### Notice

Ana wants the screen to say "Hello Ana" and to show a photo of her dog. She also wants the screen to work for a friend who uses another language, and for a friend who listens to the screen instead of looking at it.

### Guess

Where should the words and the photo come from?

- A. Typed straight into the screen's code, each time they are used.
- B. Looked up by name from one place that holds all the words and pictures.
- C. Fetched from the internet every time the screen opens.

Idea A bakes one language into the code, so a translation means hunting through every screen. Idea C can work for some photos, but it needs extra machinery. Idea B is the phrase book and the photo box.

### Build

1. Decide what the card must show: some words, and a photo.
2. Hand the screen the words. The screen draws them.
3. Keep the words in the phrase book under a name, and ask for them by name.
4. Keep the photo in the photo box under a name, and ask for it by name.
5. Write a caption for the photo, so a person who cannot see it hears what it shows.
6. If the photo is only decoration and adds nothing, say so, and the reader skips it.
7. Do not measure the photo first. Giving it a size is a separate choice.

### Name it

A **resource** is a file or an entry that lives outside your Kotlin code, in your project's `res` folder (general knowledge). Words are kept in a strings file, called `strings.xml` in the Android documentation's example. Pictures go in a drawable folder. The build makes a class called `R` that lets the code ask for each one by name (general knowledge).

**`Text`** is the composable that shows words. The Android documentation says the most basic way is to give it a `String`, as in `Text("Hello World")`. It also says to use a **string resource** instead of hard-coding the words, because you can share the same strings with your Android Views and prepare the app for internationalization. A string resource is read with **`stringResource`**.

**`Image`** is the composable that shows a picture. The documentation says to load an image from the disk, such as a PNG, JPEG or WEBP file, or a vector resource, with **`painterResource`**. A **painter** is the thing that knows how to draw a picture (general knowledge), and `Image` is given one.

The **`contentDescription`** is the caption. The documentation says to supply one for visual elements, because TalkBack reads it out. TalkBack is Android's screen reader, the feature that reads the screen aloud (general knowledge). The text must be meaningful when read out loud and translated. If the element is purely visual decoration, set it to `null`, and the screen reader ignores it.

Here are the pieces. They need the Compose libraries, so they build only inside an Android project.

```kotlin
// does not compile
// It needs the Compose libraries, so it builds only inside an Android project.
// R is made by the build from your resource folders.
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun Words() {
    Text(stringResource(R.string.hello_world))
}

@Composable
fun Photo() {
    Image(
        painter = painterResource(id = R.drawable.dog),
        contentDescription = stringResource(id = R.string.dog_content_description)
    )
}
```

The words come from `R.string.hello_world`, and the caption comes from a second string, `R.string.dog_content_description`, so the caption can be translated too. Neither call gives a size. A picture that is only decoration would pass `contentDescription = null`.

A picture from the internet is different. The documentation says to use a third-party image-loading library, and its example uses Coil's `AsyncImage` with a web address. The documentation says such libraries handle caching and networking. A plain `Image` is given a painter, and a web address is not a painter.

This page has no program to run. Compose code needs an Android project.

## Predict the output

```challenge
type: predict
run: false
id: which-line-can-be-translated
verified: not-run
code: |
  Three composables say the same words on the screen.

    A:  Text("Welcome back")
    B:  Text(text = "Welcome back")
    C:  Text(stringResource(R.string.welcome_back))

  The words are kept in a strings file with a translation for French.
  Which line shows the French words on a phone set to French?
options:
  - id: a
    text: "A"
    why: "You may have thought the shortest line is the plainest. Line A has the English words typed into the code, so the phrase book is never asked."
  - id: b
    text: "B"
    why: "You may have thought naming the parameter makes it look something up. Line B still has the English words typed into the code."
  - id: c
    text: "C"
    correct: true
    why: "Line C asks the phrase book for the words by name, so the phone can pick the French entry. The Android documentation recommends string resources for this reason."
output: |
  C
```

```challenge
type: predict
run: false
id: what-does-the-reader-say-for-null
verified: not-run
code: |
  Image(
      painter = painterResource(id = R.drawable.confetti),
      contentDescription = null
  )

  The picture is only decoration. A person uses a screen reader
  (TalkBack) and reaches this picture. What does it do?
options:
  - id: a
    text: "It reads the name of the picture file"
    why: "You may have thought a reader falls back to the file name. With null, the Android documentation says the screen reader ignores the picture."
  - id: b
    text: "It skips the picture and says nothing about it"
    correct: true
    why: "The documentation says that for a purely decorative element you set contentDescription to null for the screen reader to ignore it. That is the last button in the sim."
  - id: c
    text: "It says the word image"
    why: "You may have thought the reader always announces a picture. It reads the caption you give it, and with null there is no caption to read."
output: |
  It skips the picture and says nothing about it
```

```challenge
type: predict
run: false
id: does-the-photo-need-a-size
verified: not-run
code: |
  @Composable
  fun Photo() {
      Image(
          painter = painterResource(id = R.drawable.dog),
          contentDescription = stringResource(id = R.string.dog_content_description)
      )
  }

  No size is given anywhere. What shows on the screen?
options:
  - id: a
    text: "The dog photo, because a size is optional"
    correct: true
    why: "The documentation's own example has no size, and the sim draws the photo without asking for one. The photo has a size of its own (general knowledge). A size is a choice you may add."
  - id: b
    text: "Nothing, until a width and height in pixels are added"
    why: "You may have thought a picture needs measuring first. A size is something you can add, and the photo shows without it."
  - id: c
    text: "An error, because the picture is missing its size"
    why: "You may have thought a missing number is an error. The code has all it needs: a picture and a caption."
output: |
  The dog photo, because a size is optional
```

## Tricks to remember

- Words go in `Text`. Pictures go in `Image`. Every picture gets a caption, or a deliberate `null`.
- Ask the phrase book, not the code: `stringResource` beats typed-in words.
- A web address is not a painter. Use an image-loading library for a picture from the internet.
- Task, in an Android project with Compose: show a `Text` from a string resource and an `Image` from a drawable, then add a second language file and change the phone's language.

## Common mistakes

- **Typing the words straight into `Text`.** It is the shortest code, and it works on your phone. The documentation recommends string resources, so a translation or a shared string needs no hunt through the code. Put the words in the strings file and ask by name.
- **Leaving out the caption on a meaningful picture, or writing a vague one.** The photo looks fine to you, so it feels finished. TalkBack reads the caption aloud, so write words that make sense when spoken and translated. Use `null` only for pure decoration.
- **Passing a web address to `Image`.** It feels like it should fetch the picture. A plain `Image` is given a painter. Use an image-loading library such as Coil for a picture from the internet.
- **Thinking a picture needs a size in pixels before it shows.** Other tools ask for one, so it feels required. The documentation's example has no size. Add a size only when the layout needs one.

## Where this shows up in Android

A friend sets the phone to French. Every screen speaks French except one, which still says "Welcome back". That screen has the words typed into `Text`. A string resource would have let the phone pick a French entry. The documentation names internationalization as a reason to use string resources.

A second symptom: a user with TalkBack opens a profile screen, and the avatar is announced with nothing useful, or is silent. The photo has no caption, or a caption that says nothing. The documentation says to supply a meaningful `contentDescription`, and to use `null` only for decoration.

## Say it in an interview

**How do you display text and images in Jetpack Compose?**
I use the `Text` composable for words and the `Image` composable for a picture. The Android documentation says `Text` takes a `String`, and `Image` takes a painter, which `painterResource` makes from a file in the project. For words I prefer a string resource over typed-in text.

**How do image loading libraries like Coil work with Compose?**
For a picture from the internet, the documentation says to use a third-party library, and its example is Coil's `AsyncImage` with a web address and a content description. It says these libraries handle caching and networking, so I do not download the same picture again and again. A plain `Image` is given a painter, and a web address is not one.

**How does a content description work on an image?**
The documentation says TalkBack reads out the content description, so the text must be meaningful when read aloud and translated. For a purely decorative picture, I set it to `null` and the screen reader ignores it. I write the description as a string resource so that it can be translated.

**What goes wrong when a list loads large remote images with no resizing or caching?**
My own reasoning: each row downloads and decodes a big picture, so scrolling slows down and memory use climbs. The documentation says that image libraries handle caching, so I would use one and ask it for a size that suits the row. I would then check the result in the profiler.

## Recap

1. `Text` shows words and `Image` shows a picture, and neither needs a size to appear.
2. Keep words in string resources, so translations need no change to the code.
3. Give each meaningful picture a description, and use null only for pure decoration.

## You can now

- Predict whether a picture shows when no size is given.
- Choose the line that shows French words from three lines that say the same thing.
- Predict what a screen reader does with a picture whose description is null.
