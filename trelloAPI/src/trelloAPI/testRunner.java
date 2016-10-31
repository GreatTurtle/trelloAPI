package trelloAPI;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class testRunner 
{
	public static void main(String[] args)
	{
		testSuite.key = args[0];
		testSuite.token = args[1];
		
		Result result = JUnitCore.runClasses(testSuite.class);		
		System.out.println("\n\n-------------------------------------------------------------------------");
		System.out.println("TEST RESULTS");
		System.out.println("-------------------------------------------------------------------------");
		System.out.println("Passed: " + (result.wasSuccessful() ? "YES" : "NO"));
		System.out.println("Total Test Cases: " + result.getRunCount());
		System.out.print("Successful: " + (result.getRunCount() - result.getFailureCount() - result.getIgnoreCount()));
		System.out.print("  Ignored: " + result.getIgnoreCount());
		System.out.println("  Failed: " + result.getFailureCount());
		System.out.println("Run Time: " + ((double) result.getRunTime() / 1000) + " seconds");
		System.out.println("-------------------------------------------------------------------------");
		
		for(Failure failure: result.getFailures())
		{
			System.out.println(failure);
		}
	}
}
