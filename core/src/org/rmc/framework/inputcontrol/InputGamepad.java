package org.rmc.framework.inputcontrol;

public class InputGamepad {

    private static InputGamepad instance = null;

    private int buttonA;
    private int buttonB;
    private int buttonX;
    private int buttonY;
    private int buttonLeftShoulder;
    private int buttonRightShoulder;
    private int buttonSelect;
    private int buttonStart;
    private int buttonLeftStick;
    private int buttonRightStick;

    private int axisLeftX;
    private int axisLeftY;
    private int axisRightX;
    private int axisRightY;

    private InputGamepad() {
        this.buttonA = 0;
        this.buttonB = 1;
        this.buttonX = 2;
        this.buttonY = 3;
        this.buttonLeftShoulder = 9;
        this.buttonRightShoulder = 10;
        this.buttonSelect = 4;
        this.buttonStart = 6;
        this.buttonLeftStick = 7;
        this.buttonRightStick = 8;

        this.axisLeftX = 0;
        this.axisLeftY = 1;
        this.axisRightX = 2;
        this.axisRightY = 3;
    }

    public static InputGamepad getInstance() {
        if (instance == null)
            instance = new InputGamepad();
        return instance;
    }

    public int getButtonA() {
        return this.buttonA;
    }

    public void setButtonA(int buttonA) {
        this.buttonA = buttonA;
    }

    public int getButtonB() {
        return this.buttonB;
    }

    public void setButtonB(int buttonB) {
        this.buttonB = buttonB;
    }

    public int getButtonX() {
        return this.buttonX;
    }

    public void setButtonX(int buttonX) {
        this.buttonX = buttonX;
    }

    public int getButtonY() {
        return this.buttonY;
    }

    public void setButtonY(int buttonY) {
        this.buttonY = buttonY;
    }

    public int getButtonLeftShoulder() {
        return this.buttonLeftShoulder;
    }

    public void setButtonLeftShoulder(int buttonLeftShoulder) {
        this.buttonLeftShoulder = buttonLeftShoulder;
    }

    public int getButtonRightShoulder() {
        return this.buttonRightShoulder;
    }

    public void setButtonRightShoulder(int buttonRightShoulder) {
        this.buttonRightShoulder = buttonRightShoulder;
    }

    public int getButtonSelect() {
        return this.buttonSelect;
    }

    public void setButtonSelect(int buttonSelect) {
        this.buttonSelect = buttonSelect;
    }

    public int getButtonStart() {
        return this.buttonStart;
    }

    public void setButtonStart(int buttonStart) {
        this.buttonStart = buttonStart;
    }

    public int getButtonLeftStick() {
        return this.buttonLeftStick;
    }

    public void setButtonLeftStick(int buttonLeftStick) {
        this.buttonLeftStick = buttonLeftStick;
    }

    public int getButtonRightStick() {
        return this.buttonRightStick;
    }

    public void setButtonRightStick(int buttonRightStick) {
        this.buttonRightStick = buttonRightStick;
    }

    public int getAxisLeftX() {
        return this.axisLeftX;
    }

    public void setAxisLeftX(int axisLeftX) {
        this.axisLeftX = axisLeftX;
    }

    public int getAxisLeftY() {
        return this.axisLeftY;
    }

    public void setAxisLeftY(int axisLeftY) {
        this.axisLeftY = axisLeftY;
    }

    public int getAxisRightX() {
        return this.axisRightX;
    }

    public void setAxisRightX(int axisRightX) {
        this.axisRightX = axisRightX;
    }

    public int getAxisRightY() {
        return this.axisRightY;
    }

    public void setAxisRightY(int axisRightY) {
        this.axisRightY = axisRightY;
    }

}
