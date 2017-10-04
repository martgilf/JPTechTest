This solution processes messages, product sales and adjustments, and holds them in memory for periodic reporting.

The solution satisfies the requirements: single threaded, core Java, minimise third party libraries, etc.

The message and application design and further details are provided in the associated word document.

The application build has been automated via Eclipse Maven plugin with associated automated unit testing via JUnit.

Please refer to: 'src/main' directory for the Java code; 'src/test' for the JUnit tests; 'src/test/resources' for the JSON test files;  and, the 'surefire-reports' directory for evidence of testing and test-driven development.

The code was subject to static analysis, using SonarLint, and test/code coverage, using EclEmma, reporting.
