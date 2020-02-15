# Lab 5 Color Detection

See instructions on MyCourses and read the general guidelines on the
[website](https://mcgill-dpm.github.io/website/).

Follow the **design process** described in this course in order to implement this lab.
You could be asked to explain parts of your process during the demo.

1. Work with your team members to understand the requirements of this project,
   and come up with a **high-level design** for the hardware and software.
   These can be in the form of sketches, pseudocode, a simple diagram, and/or written descriptions.
   Validate this high-level design before building the robot or running any code on it by considering different demo scenarios,
   including edge cases.
2. Charcterize your three ultrasonic and color sensors, and select the best ones. Document this process for the project.
3. Test your software design by running your logic (as helper methods) on your computer, if you are doing any complex computations.
   See the Lab 4 FAQ for an example.
4. Gradually integrate your software features into the codebase, making sure to validate them before and after they are introduced.
   If your hardware changes, adjust your software as needed.
5. Test your robot with the different demo scenarios mentioned in step 1. Don't forget to test edge cases.

---

### See this repo's [javadoc](https://auryan898.github.io/DPM-Team-13-Repo/doc/)

## How to use

The template already includes indluded is a basic Localizer class, and a Navigation class, derived from the previous labs.  This code uses the built in features and classes that 
are included in the LeJos source code, along with some other stuff custom stuff.  

So, if you want to use this code efficiently, understand the following objects defined in `Resources`:

- `odometry` is a lejos [OdometryPoseProvider](http://lejos.org/ev3/docs/lejos/robotics/localization/OdometryPoseProvider.html)
- `pilot` is a lejos [MovePilot](http://lejos.org/ev3/docs/lejos/robotics/navigation/MovePilot.html)
- `Pose` is a lejos [class that's used a lot](http://lejos.org/ev3/docs/lejos/robotics/navigation/Pose.html)

- `comm` is from a [custom library](https://github.com/auryan898/EV3-Comm-Wrapper)
- `navigation` is just from Lab 4
- `display` is defined in the Display class
