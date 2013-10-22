package io.serialize;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import geometries.Geometry;
import structural.GeneralParameters;
import structural.Lattice;
import structural.halt.HaltCondition;
import structural.identifiers.Coordinate;

public class IntervalWriter extends Writer {

	// I/O handle for the interval file (What changed at each time step, and how long it took)
	private BufferedWriter intervalWriter;
	private final String INTERVAL_FILENAME = "interval.txt";

	private long prevTime;
	
	public IntervalWriter(GeneralParameters p, Geometry g) {
		super(p, g);
		

	}

	@Override
	public void init(Lattice l) {
		String intervalFileStr = p.getInstancePath() + '/' + INTERVAL_FILENAME;
		
		try {
			File intervalFile = new File(intervalFileStr);
			FileWriter ifw = new FileWriter(intervalFile);
			intervalWriter = new BufferedWriter(ifw, 1048576);
			intervalWriter.append("Step,Gillespie,Running time\n");
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		prevTime = System.currentTimeMillis();
	}

	@Override
	public void step(Coordinate[] highlights, double gillespie, int frame) {
		Long interval = System.currentTimeMillis() - prevTime;
		if (p.isFrame(frame)) {
			interval(frame, gillespie, interval);
		}
		
		prevTime = System.currentTimeMillis();
	}

	@Override
	public void dispatchHalt(HaltCondition ex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		try {
			intervalWriter.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Wall clock time and simulation time for last time step.
	 * 
	 * @param simInterval
	 * @param realInterval
	 */
	private void interval(int n, double gillespie, long interval) {
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		sb.append(',');
		sb.append(gillespie);
		sb.append(',');
		sb.append(interval);
		sb.append('\n');
		try {
			intervalWriter.append(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
}
