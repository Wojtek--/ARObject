package com.mgr;

import java.io.Serializable;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;


public class Group implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String materialName = "default";
	private transient Material material;
	
	private boolean textured = false;
	public transient FloatBuffer vertices = null;
	public transient FloatBuffer textcoords = null;
	public transient FloatBuffer normals = null;
	public int vertexCount = 0;
	
	public ArrayList<Float> groupVertices = new ArrayList<Float>(500);
	public ArrayList<Float> groupNormals = new ArrayList<Float>(500);
	public ArrayList<Float> groupTextcoords = new ArrayList<Float>();
	
	public Group() {
	}
	
	public void setMaterialName(String currentMaterial) {
		this.materialName = currentMaterial;
	}
	
	public String getMaterialName() {
		return materialName;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public void setMaterial(Material material) {
		if (textcoords != null && material != null && material.hasTexture()) {
			textured = true;
		}
		if (material != null) {
			this.material = material;
		}
	}
	
	public boolean containsVertices() {
		if (groupVertices != null) {
			return groupVertices.size() > 0;
		} else if (vertices != null) {
			return vertices.capacity() > 0;
		} else {
			return false;
		}
	}
	
	public void setTextured(boolean b) {
		textured = b;
	}
	
	public boolean isTextured() {
		return textured;
	}

	public void finalize() {
		if (groupTextcoords.size() > 0) {
			textured = true;
			textcoords = MemUtil.makeFloatBuffer(groupTextcoords.size());
			for (Iterator<Float> iterator = groupTextcoords.iterator(); iterator.hasNext();) {
				Float curVal = iterator.next();
				textcoords.put(curVal.floatValue());				
			}
			textcoords.position(0);
			if(material != null && material.hasTexture()) {
				textured = true;
			} else {
				textured = false;
			}
		}
		groupTextcoords = null;
		vertices = MemUtil.makeFloatBuffer(groupVertices.size());
		vertexCount = groupVertices.size()/3;//three floats pers vertex
		for (Iterator<Float> iterator = groupVertices.iterator(); iterator.hasNext();) {
			Float curVal = iterator.next();
			vertices.put(curVal.floatValue());
		}
		groupVertices = null;
		normals = MemUtil.makeFloatBuffer(groupNormals.size());
		for (Iterator<Float> iterator = groupNormals.iterator(); iterator.hasNext();) {
			Float curVal =  iterator.next();
			normals.put(curVal.floatValue());
		}
		groupNormals = null;
		vertices.position(0);
		normals.position(0);		
	}
	
}
