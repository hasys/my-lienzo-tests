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
    private WiresShape parentShape;
    private static final String FAMILY = "Verdana";
    private static final String COLOR = "#000000";
    private static final int FONT_SIZE = 12;
    private static final int WIDTH = 600;
    private static final int HEIGHT = WIDTH;
    private static final int MARGIN = WIDTH/10;

    public void test(Layer _layer) {
        this.layer = _layer;
        final WiresManager wires_manager = WiresManager.get(layer);

        final MultiPath parentMultiPath = new MultiPath()
                .rect(MARGIN, MARGIN, WIDTH, HEIGHT)
                .setFillColor("#006400")
                .setFillAlpha(0.1)
                .setDraggable(false);
        parentShape = new WiresShape(parentMultiPath);
        parentMultiPath.addNodeMouseEnterHandler(new NodeMouseEnterHandler() {
            @Override
            public void onNodeMouseEnter(NodeMouseEnterEvent event) {
                showEventText("Enter", event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseExitHandler(new NodeMouseExitHandler() {
            @Override
            public void onNodeMouseExit(NodeMouseExitEvent event) {
                showEventText("Exit", event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseOverHandler(new NodeMouseOverHandler() {
            @Override
            public void onNodeMouseOver(NodeMouseOverEvent event) {
                showEventText("Over", event.getX(), event.getY());
            }
        });

        parentMultiPath.addNodeMouseOutHandler(new NodeMouseOutHandler() {
            @Override
            public void onNodeMouseOut(NodeMouseOutEvent event) {
                showEventText("Out", event.getX(), event.getY());
            }
        });


        int width = WIDTH - MARGIN;
        int height = HEIGHT - MARGIN;
        WiresShape rectangle = createRectangle(parentShape, width, height);
        for (int i = 0; i < 6; i++) {
            rectangle = createRectangle(rectangle, width -= MARGIN, height -= MARGIN);
        }

        wires_manager.register(parentShape);
        batch();
    }

    private void showEventText(String text, int x, int y) {
        final Text eventText = new Text(text)
                .setX(x)
                .setY(y)
                .setFontFamily(FAMILY)
                .setFontSize(FONT_SIZE)
                .setStrokeColor(COLOR)
                .setVisible(true);

        parentShape.addChild(eventText);
        batch();
        GWT.log("Event fired: " + text);

        Timer t = new Timer() {
            @Override
            public void run() {
                parentShape.removeChild(eventText);
                batch();
            }
        };

        t.schedule(1000);
    }

    private WiresShape createRectangle(WiresShape parent, int width, int height) {
        MultiPath mp = new MultiPath()
                .rect((WIDTH-width)/2 + MARGIN, MARGIN/2, width, height)
                .setFillColor("#006400")
                .setFillAlpha(parent.getPath().getFillAlpha() + 0.1)
                .setDraggable(false);

        WiresShape result = new WiresShape(mp);
        parent.add(result);

        return result;
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
