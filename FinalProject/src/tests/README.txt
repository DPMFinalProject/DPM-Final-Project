Procedure for creating a test:

1.	Make a test class extending TestCase
2.	Override the function runTest, which will act as your main
3.	Go into TestMain.java, line 26 is:
		TestCase test = new ExampleTest();
	modify it to
		TestCase test = new YourTestClass();
4.	Run TestMain


Procedure for getting data from the NXT:

System.out.println will print to the console when you run tests, assuming it is
connected properly.

Mac:
Run nxjconsole from the command line when the NXT prompts to connect to a console.

Windows:
Should be the same thing from the command prompt, but I cannot confirm.
It also should be able to work with bluetooth, feel free to try it. 
Talk to me if you can't get it to work. 