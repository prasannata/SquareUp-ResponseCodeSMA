package org.prasanna.square;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ResponseCodeSMA
{
    private final int[] count = new int[6];

    private final double[] sma = new double[6];

    private final int SAMPLE_SIZE = 5;

    private final int RECORD_INTERVAL = 10;

    private final Map<Integer, List<Integer>> samples;

    private int numResponses = 0;

    private static ResponseCodeSMA responseCodeSMA = new ResponseCodeSMA();

    private ResponseCodeSMA()
    {
        samples = new HashMap<Integer, List<Integer>>(SAMPLE_SIZE);
        init();
    }

    public static ResponseCodeSMA getInstance()
    {
        return responseCodeSMA;
    }

    private void init()
    {
        for (int i = 0; i < 6; i++)
        {
            count[i] = 0;
            samples.put(i, new LinkedList<Integer>());
        }
    }

    public void record(int responseCode)
    {
        int errorClass = responseCode / 100;

        if (errorClass >= 1 && errorClass <= 5)
        {
            count[errorClass]++;
        }

        if (++numResponses == RECORD_INTERVAL)
        {
            calculateSMA();
            numResponses = 0;
        }
    }

    private void calculateSMA()
    {
        System.out.println("-----------------------------");
        System.out.println("In last " + (RECORD_INTERVAL * SAMPLE_SIZE) + " requests...");

        for (int responseClass = 1; responseClass < 6; responseClass++)
        {
            Integer lastRemoved = null;
            LinkedList<Integer> values = (LinkedList<Integer>) samples.get(responseClass);

            if (values.size() == SAMPLE_SIZE)
            {
                lastRemoved = values.removeFirst();
            }

            values.addLast(count[responseClass]);
            samples.put(responseClass, values);
            count[responseClass] = 0;

            if (values.size() == SAMPLE_SIZE)
            {
                double sum = 0;

                if (sma[responseClass] == 0)
                {
                    for (Integer value : values)
                    {
                        sum += value;
                    }

                    sma[responseClass] = sum / SAMPLE_SIZE;
                }
                else
                {
                    sma[responseClass] =
                            (sma[responseClass] - (lastRemoved / SAMPLE_SIZE)) + (values.getLast() / SAMPLE_SIZE);
                }

                System.out.println(responseClass + "XX SMA = " + sma[responseClass] + " every " + RECORD_INTERVAL
                        + " requests, variance = " + calculateVariance(responseClass, values));
            }
        }

        System.out.println();
    }

    private double calculateVariance(int responseClass, LinkedList<Integer> values)
    {
        double sum = 0;

        for (Integer value : values)
        {
            sum += (sma[responseClass] - value) * (sma[responseClass] - value);
        }

        return (sum / SAMPLE_SIZE);
    }
}