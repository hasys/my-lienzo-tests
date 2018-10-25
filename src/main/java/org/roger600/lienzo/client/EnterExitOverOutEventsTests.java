package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.event.NodeMouseEnterEvent;
import com.ait.lienzo.client.core.event.NodeMouseEnterHandler;
import com.ait.lienzo.client.core.event.NodeMouseExitEvent;
import com.ait.lienzo.client.core.event.NodeMouseExitHandler;
import com.ait.lienzo.client.core.event.NodeMouseOutEvent;
import com.ait.lienzo.client.core.event.NodeMouseOutHandler;
import com.ait.lienzo.client.core.event.NodeMouseOverEvent;
import com.ait.lienzo.client.core.event.NodeMouseOverHandler;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.Text;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

public class EnterExitOverOutEventsTests extends FlowPanel implements MyLienzoTest,
                                                                      HasButtons,
                                                                      HasMediators {

    private Layer layer;
    private static final String FAMILY = "Verdana";
    private static final String BLACK_COLOR = "#000000";
    private static final int FONT_SIZE = 12;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int MARGIN = WIDTH/10;

    public void test(Layer _layer) {
        this.layer = _layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        final MultiPath parentMultiPath = new MultiPath()
                .rect(MARGIN, MARGIN, WIDTH, HEIGHT)
                .setFillColor("#006400")
                .setFillAlpha(0.1)
                .setDraggable(false);
        WiresShape parentShape = new WiresShape(parentMultiPath);
        parentMultiPath.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            @Override
            public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                showEventText("Enter", BLACK_COLOR, event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseExitHandler(new NodeMouseExitHandler() {
            @Override
            public void onNodeMouseExit(NodeMouseExitEvent event) {
                showEventText("Exit", BLACK_COLOR, event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseOverHandler(new NodeMouseOverHandler() {
            @Override
            public void onNodeMouseOver(NodeMouseOverEvent event) {
                showEventText("Over", BLACK_COLOR, event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseOutHandler(new NodeMouseOutHandler() {
            @Override
            public void onNodeMouseOut(NodeMouseOutEvent event) {
                showEventText("Out", BLACK_COLOR, event.getX(), event.getY());
            }
        });


        int width = WIDTH - MARGIN;
        int height = HEIGHT - MARGIN;
        WiresShape rectangle = createRectangle(parentShape, width, height);
        for (int i = 0; i < 3; i++) {
            rectangle = createRectangle(rectangle, width -= MARGIN, height -= MARGIN);
        }

        width = width/2 + MARGIN;
        height = height/2 + MARGIN;

        rectangle.add(getShape(rectangle, WIDTH + MARGIN - width*2, MARGIN, width, height));
        rectangle.add(getShape(rectangle, WIDTH + MARGIN - width, MARGIN, width, height));

        final MultiPath parentMultiPath2 = new MultiPath()
                .rect( WIDTH + MARGIN, MARGIN, WIDTH/2.0, HEIGHT)
                .setFillColor("#000064")
                .setFillAlpha(0.7)
                .setDraggable(false);
        WiresShape parentShape2 = new WiresShape(parentMultiPath2);

        wires_manager.register(parentShape);
        wires_manager.register(parentShape2);
        batch();
    }

    private void showEventText(String text, String color, int x, int y) {
        final Text eventText = new Text(text)
                .setX(x)
                .setY(y)
                .setFontFamily(FAMILY)
                .setFontSize(FONT_SIZE)
                .setStrokeColor(color)
                .setVisible(true);

        layer.add(eventText);
        eventText.moveToBottom();
        batch();
        GWT.log("Event fired: " + text);

        Timer t = new Timer() {
            @Override
            public void run() {
                layer.remove(eventText);
                batch();
            }
        };

        t.schedule(1000);
    }

    private WiresShape createRectangle(WiresShape parent, int width, int height) {
        WiresShape result = getShape(parent, WIDTH - width + MARGIN, MARGIN, width, height);
        parent.add(result);

        return result;
    }

    private WiresShape getShape(WiresShape parent, int x, int y, int width, int height) {
        MultiPath mp = new MultiPath()
                .rect(x, y, width, height)
                .setFillColor("#006400")
                .setFillAlpha(parent.getPath().getFillAlpha() + 0.1)
                .setDraggable(false);

        return new WiresShape(mp);
    }

    @Override
    public void setButtonsPanel(Panel panel) {

        Button buttonRR = new Button("Remove Rect");
        buttonRR.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
            }
        });

        panel.add(buttonRR);
    }

    private void batch() {
        layer.batch();
    }
}
