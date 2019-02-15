# Contributing to the OME Model

The OME Model is licensed under the
[2-clause BSD license](https://opensource.org/licenses/BSD-2-Clause) and
accepts contributions via GitHub pull requests. This document outlines some of
the conventions on development workflow, commit message formatting, contact
points and other resources to make it easier to get your contribution accepted.

Guidance for contributing to OME projects in general can be found at
https://docs.openmicroscopy.org/contributing/

## Certificate of Origin

By contributing to this project you agree to the
[Developer Certificate of Origin](https://developercertificate.org/) (DCO).
This document was created by the Linux Kernel community and is a simple
statement that you, as a contributor, have the legal right to make the
contribution. See the [DCO](DCO) file for details.

## Making a change

Before making any significant changes, please
[open an issue](http://github.com/ome/ome-model/issues/).

* Fork the GitHub repository and create a branch for your work.
* Make your commits, test your changes locally as per the README, and open a
  PR.
* Make sure you include details of the problem you are fixing and how to test
  your changes.

## Sign your work

The sign-off is a simple line at the end of the explanation for the patch, 
which certifies that you wrote it or otherwise have the right to pass it on as
an open-source patch. If you can certify the [DCO](DCO), add a line to every
Fit commit message:

    Signed-off-by: Joe Smith <joe@gmail.com>

using your real name and institutional address.

You can add the sign off when creating the git commit via `git commit -s`.

If you want this to be automatic you can set up some aliases:

    git config --add alias.amend "commit -s --amend"
    git config --add alias.c "commit -s"

## Review process

* PRs submitted from outside OME will get an initial review to identify if
  they are suitable to pass into our continuous integration system for
  building and testing. We try to do this within 2 days of submission but
  please be patient if we are busy and it takes longer.

* If there are any obvious issues, we will comment and wait for you to fix
  them. You can help this process by ensuring that the Travis build is passing
  when you first submit the PR.

* Once we are confident the PR contains no obvious errors, an "include" label
  will be added which means the PR will be included in the merge build jobs
  for the appropriate branch.

* Build failures will then be noted on the PR and we will either submit a
  patch or provide sufficient information for you to fix the problem yourself.
  The "include" label will be removed until this is completed.

* The PR will be merged once all the builds are green with the "include" label
  added.

