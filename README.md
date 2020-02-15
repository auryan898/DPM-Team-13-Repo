# DPM Team 13 Sample Code

A true sample code template that includes a basic motor controller, odometry tracker, 
and *custom* navigation class, along with optional sensor reading classes.

### see this repo's [javadoc](https://auryan898.github.io/DPM-Team-13-Repo/doc/)

## How to use this template

The template already includes a basic Localizer class, and a Navigation class, derived from the previous labs.  This template uses the built in features and classes that are included in the LeJos source code, along with some other stuff I wrote.  

So, if you want to use this code efficiently, understand the following objects defined in `Resources`:

- `odometry` is a lejos [OdometryPoseProvider](http://lejos.org/ev3/docs/lejos/robotics/localization/OdometryPoseProvider.html)
- `pilot` is a lejos [MovePilot](http://lejos.org/ev3/docs/lejos/robotics/navigation/MovePilot.html)
- `Pose` is a lejos [class that's used a lot](http://lejos.org/ev3/docs/lejos/robotics/navigation/Pose.html)

- `comm` is from a [custom library](https://github.com/auryan898/EV3-Comm-Wrapper)
- `navigation` is just from Lab 4
- `display` is defined in the Display class
