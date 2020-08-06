package nl.debijenkorf.assignment.imageservice.beans;

public class ImageType {

	public enum ScaleType {
		CROP,
		FILL,
		SKEW
	}

	public enum Type {
		JPG,
		PNG
	}

	private int height;
	private int width;
	private int quality;
	private ScaleType scaleType;
	private String fillColor;
	private Type type;
	private String name;

	public ImageType(final int height, final int width, final int quality, final ScaleType scaleType, final String fillColor, final Type type, final String name) {
		this.height = height;
		this.width = width;
		setQuality(quality);
		this.scaleType = scaleType;
		this.fillColor = fillColor;
		this.type = type;
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(final int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public int getQuality() {
		return quality;
	}

	public void setQuality(final int quality) {
		this.quality = quality > 100 ? 100 : quality;
	}

	public ScaleType getScaleType() {
		return scaleType;
	}

	public void setScaleType(final ScaleType scaleType) {
		this.scaleType = scaleType;
	}

	public String getFillColor() {
		return fillColor;
	}

	public void setFillColor(final String fillColor) {
		this.fillColor = fillColor;
	}

	public Type getType() {
		return type;
	}

	public void setType(final Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
