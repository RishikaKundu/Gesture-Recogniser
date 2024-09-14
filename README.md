# Gesture-Recogniser

_This project was a part of a College Assignment_

This project involves developing a 2D gesture recognition system using the [$1 Gesture Recognizer](https://dl.acm.org/doi/pdf/10.1145/1294211.1294238?casa_token=FFmOOdPtTt8AAAAA:-ftV2njC_TBSh4TjLV0wc0oSCDoUxows6_X_ILvMM-31cPcaq93S9y_xtKN2sPjpWsyXxFL_9UAh) algorithm. The app allows users to draw gestures like arrows or circles, and recognizes them by comparing the input against stored gesture templates.

https://dl.acm.org/doi/pdf/10.1145/1294211.1294238?casa_token=FFmOOdPtTt8AAAAA:-ftV2njC_TBSh4TjLV0wc0oSCDoUxows6_X_ILvMM-31cPcaq93S9y_xtKN2sPjpWsyXxFL_9UAh

Key tasks include implementing gesture drawing on a canvas using mouse event handlers and completing the recognition process by resampling points, rotating gestures to align with the x-axis, scaling to a square, and translating to a common point. The recognition algorithm uses path-distance calculations to match user input with predefined templates. The project emphasizes object-oriented design, data structures, and algorithm implementation.

**Things I Learned:**

* Capturing and drawing gestures with mouse event handlers.
* Using Deques for efficient gesture data storage.
* Applying the $1 Gesture Recognizer steps: resampling, rotation, scaling, and translation.
* Debugging with visual aids and structured tests.
* Building modular, object-oriented software for gesture recognition.
* Minimizing recognition errors with path-distance calculations.
