package com.example.vaadinjvxset;

import java.io.InputStream;

import javax.servlet.annotation.WebServlet;

import com.sibvisions.vaadin.server.DownloaderExtension;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.DownloadStream;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("asteriacultura")
@Widgetset("com.sibvisions.vaadin.Widgetset")
public class VaadinjvxsetUI extends UI {

    private DownloaderExtension downloader;

    @WebServlet(value = "/download/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = VaadinjvxsetUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Button button = new Button("Report download");
//        button.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent clickEvent) {
//                int a=87;
//            }
//        });

        button.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                layout.addComponent(new Label("Your download should start automatically within 5 seconds :)"));

                downloader.setDownloadResource(new StreamResource(
                                                       new StreamResource.StreamSource() {
                                                           public InputStream getStream() {
                                                               return VaadinjvxsetUI.class.getResourceAsStream("/com/example/vaadinjvxset/jvx.png");
                                                           }
                                                       },
                                                       "jvx.png") {
                                                   @Override
                                                   public DownloadStream getStream() {
                                                       DownloadStream ds = new DownloadStream(getStreamSource().getStream(), getMIMEType(), getFilename());

                                                       // Content-Disposition: attachment generally forces download
                                                       ds.setParameter("Content-Disposition", "attachment; filename=\"" + getFilename() + "\"");
                                                       ds.setContentType("application/octet-stream;charset=UTF-8");

                                                       return ds;
                                                   }
                                               }
                );
            }
        });

        layout.addComponent(button);

        downloader = new DownloaderExtension();
        downloader.extend(this);
    }

}