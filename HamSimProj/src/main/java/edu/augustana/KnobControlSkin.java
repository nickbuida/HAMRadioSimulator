package edu.augustana;

import javafx.scene.control.SkinBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

public class KnobControlSkin extends SkinBase<KnobControl> {
    private final ImageView knobImageView;
    private final Rotate rotate;
    private double lastAngle;
    private boolean dragging;
    private final Pane dragOverlay;

    private static final double MAX_ANGLE = 225;
    private static final double MIN_ANGLE = 0;

    public KnobControlSkin(KnobControl control) {
        super(control);

        // Load the knob image
        Image knobImage = new Image(getClass().getResourceAsStream("/assets/knob.png"));
        knobImageView = new ImageView(knobImage);

        // Adjust size and scaling
        knobImageView.setFitWidth(50);
        knobImageView.setFitHeight(50);

        // Set up rotation transform
        rotate = new Rotate(0, knobImageView.getFitWidth() / 2, knobImageView.getFitHeight() / 2);
        knobImageView.getTransforms().add(rotate);

        // Create overlay pane
        dragOverlay = new Pane();
        dragOverlay.setStyle("-fx-background-color: transparent;");
        dragOverlay.setMouseTransparent(false); // Capture mouse events
        dragOverlay.setPickOnBounds(true);

        // Add the knob image and overlay to the skin
        getChildren().addAll(knobImageView, dragOverlay);

        // Handle mouse events
        setupMouseHandlers();

        // Handle scroll events
        setupScrollHandlers();
    }

    private void setupMouseHandlers() {
        double centerX = knobImageView.getFitWidth() / 2;
        double centerY = knobImageView.getFitHeight() / 2;

        dragOverlay.setOnMousePressed(event -> {
            dragging = true;
            double mouseX = event.getX() - (knobImageView.getLayoutX() + centerX);
            double mouseY = event.getY() - (knobImageView.getLayoutY() + centerY);
            lastAngle = Math.toDegrees(Math.atan2(mouseY, mouseX));
        });

        dragOverlay.setOnMouseDragged(event -> {
            if (dragging) {
                // Calculate mouse position relative to knob center
                double mouseX = event.getX() - (knobImageView.getLayoutX() + centerX);
                double mouseY = event.getY() - (knobImageView.getLayoutY() + centerY);

                // Calculate the current angle
                double currentAngle = Math.toDegrees(Math.atan2(mouseY, mouseX));

                // Compute the delta angle while handling 0/360 boundary
                double deltaAngle = currentAngle - lastAngle;
                if (deltaAngle > 180) {
                    deltaAngle -= 360;
                } else if (deltaAngle < -180) {
                    deltaAngle += 360;
                }

                // Update the rotation angle with limits
                double newAngle = Math.min(MAX_ANGLE, Math.max(MIN_ANGLE, rotate.getAngle() + deltaAngle));
                rotate.setAngle(newAngle);

                // Update the control's value property
                double normalizedValue = (newAngle / MAX_ANGLE) * 100;
                getSkinnable().setValue(normalizedValue);

                // Update the last angle
                lastAngle = currentAngle;
            }
        });

        dragOverlay.setOnMouseReleased(event -> dragging = false);
    }

    private void setupScrollHandlers() {
        dragOverlay.setOnScroll(event -> {
            double deltaAngle = event.getDeltaY() > 0 ? 5 : -5; // Adjust rotation step (scroll sensitivity)
            double newAngle = Math.min(MAX_ANGLE, Math.max(MIN_ANGLE, rotate.getAngle() + deltaAngle));
            rotate.setAngle(newAngle);

            // Update the control's value property
            double normalizedValue = (newAngle / MAX_ANGLE) * 100;
            getSkinnable().setValue(normalizedValue);
        });
    }

    public void rotateToPosition(int i) {
    }
}
