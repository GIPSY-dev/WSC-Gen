## What is the project about?

The project is about testing of various automated web service composition algorithms and
the corresponding web serviece challenge generation problems. Its datasets were used
in serveral publications. We are extending this project with contraints and effects
from the recent research, a working service respository, and making the generator
a service itself.

Originally, the project came as a fruit from multiple organization solving web service
composition challenges (WSC) implementing planning graph web services composition algorithms
for comparative studies. That included studies and comparison of the performance of
existing/contributed algorithms with planning graph and served as an experimental
reference implementatio framework to improve planning graph algorithms.

## Instructions

### Eclipse ###

* Clone and import it to `Eclipse` and use `Existing Projects into Workspace` and then configure the build path and add required jars to the class path.
`de.vs.unikassel.genrator.gui.GeneratorGUI` is the Java class that instantiates the GUI for `WSC-Gen`.

### Branches ###

* [Active Branches](https://github.com/GIPSY-dev/WSC-Gen/branches)
* [`temp`] -- this branch currently provides a manual hardcoded extension to the `WSC-Generator` in terms of extending the SLA's to include web service contraints and their effects for the recent work of Touraj Laleh. 
* [`Datasets`] -- this branch extends the `WSC-Gen` by providing updates to bring it up to recent JDK version, GUI modifications (not significant), additional toggle for the web services to be **Context-Aware** and related classes for a **GIPSY Service** by SOEN487 students.

## TODO ##

* [Issues](https://github.com/GIPSY-dev/WSC-Gen/issues)
* [Pull requests](https://github.com/GIPSY-dev/WSC-Gen/pulls)
* References to theses and papers of Ludeng and Touraj and co-authors
* References to the relevant GIPSY publications, at ICSOC and elsewhere
* Ongoing project work on the service repository

## How do I give comments/suggestions/questions?

* Open an issue in the [Issue Tracker](https://github.com/GIPSY-dev/WSC-Gen/issues)
* Please send an to the current maintainers: :e-mail: `gipsy AT encs.concordia.ca`

## Legacy README

#### What was the original project about?

The project was originally about implementing planning graph web services composition algorithm on the platform provided by Web Service Challenge 09. 
Also, the project aim to study and compare the performence of existing algorithms with planning graph and serve as an experiment basis to
improve planning graph algorithm.

#### Previous maintainer

* [Ludeng Zhao (Eric)](https://github.com/hhkb)

## References

* [WSC'09 Challenge Official Website (arhive)](https://web.archive.org/web/20131115023242/http://ws-challenge.georgetown.edu/wsc09/index.html)

