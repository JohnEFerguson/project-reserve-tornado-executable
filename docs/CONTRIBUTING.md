# CONTRIBUTING
## Requesting a feature or enhancement
Make full use of markdown capabilities when requesting a new feature.  Be descriptive and clear, and try to include examples, sketches, prototypes, etc.
 
Please file feature requests [here](https://github.com/JohnEFerguson/project-reserve/issues) and tag as `enhancement`. 

## File a bug
Please file bug reports [here](https://github.com/JohnEFerguson/project-reserve/issues) and label as `bug`. Also consider creating a test case that recreates the bug.

When creating your bug report, follow the example [here](http://www.noverse.com/blog/2012/06/how-to-write-a-good-bug-report/).
 
## Pull Requests
#### Styling
* Use Tabs, not spaces
* Tab size: 4 spaces
* Absolutely no wildcard imports
* Readability trumps conciseness!

#### Tests
Please make sure tests pass.

#### Documentation
Comment all added code in order to create [Javadocs](http://www.oracle.com/technetwork/java/javase/documentation/index-jsp-135444.html).
If either [CONTRIBUTING.md](CONTRIBUTING.md) or [README.md](README.md) should be updated as a result of code changes, make sure to do so.

If adding a new requirement to `build.gradle`, please create a short blurb about why it is needed in `build.gradle` as
well as providing a link to the requirement's homepage under "Requirements" in [README.md](README.md).

#### Commits
Follow the guidelines specified in [COMMIT_MESSAGE_CONVENTION.md](COMMIT_MESSAGE_CONVENTION.md)

Please take care to squash commits into logically separated pieces of work.  Commits for fixing typos, etc. are very annoying to wade through!

BE SURE NOT TO SQUASH COMMITS ALREADY PRESENT ON REMOTE BRANCH!

#### Pull Requests
For pull request titles, add a (refs #<your issue number here>) to make sure issues follow related merge requests.