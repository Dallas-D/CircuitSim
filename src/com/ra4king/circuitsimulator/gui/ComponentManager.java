package com.ra4king.circuitsimulator.gui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.ra4king.circuitsimulator.gui.peers.AdderPeer;
import com.ra4king.circuitsimulator.gui.peers.ClockPeer;
import com.ra4king.circuitsimulator.gui.peers.ControlledBufferPeer;
import com.ra4king.circuitsimulator.gui.peers.MultiplexerPeer;
import com.ra4king.circuitsimulator.gui.peers.PinPeer;
import com.ra4king.circuitsimulator.gui.peers.RAMPeer;
import com.ra4king.circuitsimulator.gui.peers.RegisterPeer;
import com.ra4king.circuitsimulator.gui.peers.SplitterPeer;
import com.ra4king.circuitsimulator.gui.peers.gates.AndGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.NandGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.NorGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.NotGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.OrGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.XnorGatePeer;
import com.ra4king.circuitsimulator.gui.peers.gates.XorGatePeer;
import com.ra4king.circuitsimulator.simulator.utils.Pair;

import javafx.scene.image.Image;

/**
 * @author Roi Atalla
 */
public class ComponentManager {
	private List<ComponentLauncherInfo> components;
	
	public static class ComponentLauncherInfo {
		public final Class<? extends ComponentPeer<?>> clazz;
		public final Pair<String, String> name;
		public final Image image;
		public final Properties properties;
		public final ComponentCreator<?> creator;
		
		ComponentLauncherInfo(Class<? extends ComponentPeer<?>> clazz,
		                      Pair<String, String> name,
		                      Image image,
		                      Properties properties,
		                      ComponentCreator<?> creator) {
			this.clazz = clazz;
			this.name = name;
			this.image = image;
			this.properties = properties;
			this.creator = creator;
		}
	}
	
	public interface ComponentManagerInterface {
		void addComponent(Pair<String, String> name, Image image, Properties defaultProperties);
	}
	
	static <T extends ComponentPeer<?>> ComponentCreator<T> forClass(Class<T> clazz) {
		return (properties, x, y) -> {
			try {
				return clazz.getConstructor(Properties.class, Integer.TYPE, Integer.TYPE)
				            .newInstance(properties, x, y);
			} catch(NoSuchMethodException exc) {
				throw new RuntimeException("Must have constructor taking (Properties props, int x, int y");
			} catch(Exception exc) {
				throw new RuntimeException(exc);
			}
		};
	}
	
	public ComponentManager() {
		components = new ArrayList<>();
		registerDefaultComponents();
	}
	
	public ComponentLauncherInfo get(Pair<String, String> name) {
		for(ComponentLauncherInfo component : components) {
			if(component.name.equals(name)) {
				return component;
			}
		}
		
		return null;
	}
	
	public void forEach(Consumer<ComponentLauncherInfo> consumer) {
		components.forEach(consumer);
	}
	
	public <T extends ComponentPeer<?>> void register(Class<T> clazz) {
		try {
			ComponentCreator<?> creator = forClass(clazz);
			
			Method method = clazz.getMethod("installComponent", ComponentManagerInterface.class);
			method.invoke(null,
			              (ComponentManagerInterface)(name, image, defaultProperties) ->
					                                         components.add(new ComponentLauncherInfo(clazz,
					                                                                                  name,
					                                                                                  image,
					                                                                                  defaultProperties,
					                                                                                  creator)));
		} catch(NoSuchMethodException exc) {
			throw new RuntimeException("Must implement static void installComponent(ComponentManagerInterface)");
		} catch(Exception exc) {
			throw new RuntimeException(exc);
		}
	}
	
	private void registerDefaultComponents() {
		register(PinPeer.class);
		register(ClockPeer.class);
		register(SplitterPeer.class);
		
		register(AndGatePeer.class);
		register(NandGatePeer.class);
		register(OrGatePeer.class);
		register(NorGatePeer.class);
		register(XorGatePeer.class);
		register(XnorGatePeer.class);
		register(NotGatePeer.class);
		register(ControlledBufferPeer.class);
		
		register(RegisterPeer.class);
		register(RAMPeer.class);
		register(MultiplexerPeer.class);
		register(AdderPeer.class);
	}
	
	public interface ComponentCreator<T extends ComponentPeer<?>> {
		T createComponent(Properties properties, int x, int y);
	}
}
