# Procedure for creating a test:

-	Make a test class extending TestCase
-	Override the function runTest, which will act as your main
-	Go into TestMain.java, Modify line 26 from: 
```java
TestCase test = new ExampleTest();
```  
to 
```java
TestCase test = new YourTestClass();
```
-	Run TestMain


# Procedure for getting data from the NXT:

```java
System.out.println()
``` 
will print to the console when you run tests, assuming it is
connected properly.

### Mac:
Run nxjconsole from the command line when the NXT prompts to connect to a console.

### Windows:
Should be the same thing from the command prompt, but I cannot confirm.
It also should be able to work with bluetooth, feel free to try it. 
Talk to me if you can't get it to work.