package com.ra4king.circuitsimulator.gui.peers.gates;

import com.ra4king.circuitsimulator.gui.Properties;
import com.ra4king.circuitsimulator.simulator.Circuit;
import com.ra4king.circuitsimulator.simulator.CircuitState;
import com.ra4king.circuitsimulator.simulator.components.gates.Gate;
import com.ra4king.circuitsimulator.simulator.components.gates.NandGate;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * @author Roi Atalla
 */
public class NandGatePeer extends GatePeer {
	public NandGatePeer(Circuit circuit, Properties properties, int x, int y) {
		super(circuit, properties, x, y);
	}
	
	@Override
	public Gate getGate(Circuit circuit, Properties properties) {
		properties.ensureProperty(Properties.LABEL);
		properties.ensureProperty(Properties.BITSIZE);
		properties.ensureProperty(Properties.NUM_INPUTS);
		return circuit.addComponent(new NandGate(properties.getValue(Properties.LABEL),
		                                         properties.getIntValue(Properties.BITSIZE),
		                                         properties.getIntValue(Properties.NUM_INPUTS)));
	}
	
	@Override
	public void paint(GraphicsContext graphics, CircuitState circuitState) {
		graphics.beginPath();
		graphics.moveTo(getScreenX(), getScreenY());
		graphics.lineTo(getScreenX(), getScreenY() + getScreenHeight());
		graphics.arc(getScreenX() + getScreenWidth() * 0.3, getScreenY() + getScreenHeight() * 0.5,
		             getScreenWidth() * 0.5, getScreenHeight() * 0.5, 270, 180);
		graphics.closePath();
		
		graphics.setFill(Color.WHITE);
		graphics.setStroke(Color.BLACK);
		graphics.fill();
		graphics.stroke();
		
		graphics.fillOval(getScreenX() + getScreenWidth() * 0.8,
		                  getScreenY() + getScreenHeight() * 0.5 - getScreenWidth() * 0.1, getScreenWidth() * 0.2,
		                  getScreenWidth() * 0.2);
		graphics.strokeOval(getScreenX() + getScreenWidth() * 0.8,
		                    getScreenY() + getScreenHeight() * 0.5 - getScreenWidth() * 0.1, getScreenWidth() * 0.2,
		
		                    getScreenWidth() * 0.2);
	}
}
