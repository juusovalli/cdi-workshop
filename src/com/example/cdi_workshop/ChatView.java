package com.example.cdi_workshop;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class ChatView extends CustomComponent implements View {

    private UserDAO userDAO = new TestingUserDAO();

    private MessageService messageService = new MessageServiceImpl();

    private User targetUser;

    private Layout messageLayout;

    private static final int MAX_MESSAGES = 16;

    @Override
    public void enter(ViewChangeEvent event) {
        String parameters = event.getParameters();
        Layout layout;
        if (parameters.isEmpty()) {
            targetUser = null;
            layout = buildUserSelectionLayout();
        } else {
            targetUser = userDAO.getUserBy(parameters);
            if (targetUser == null) {
                layout = buildErrorLayout();
            } else {
                layout = buildUserLayout();
            }
        }
        setCompositionRoot(layout);
    }

    private Layout buildErrorLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("No such user"));
        layout.addComponent(generateBackButton());
        return layout;
    }

    private Layout buildUserLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Talking to " + targetUser.getName()));
        layout.addComponent(generateBackButton());
        layout.addComponent(buildChatLayout());
        return layout;
    }

    private Component buildChatLayout() {
        VerticalLayout chatLayout = new VerticalLayout();
        chatLayout.setSizeFull();
        chatLayout.setSpacing(true);
        messageLayout = new VerticalLayout();
        messageLayout.setWidth("100%");
        for (Message message : messageService.getLatestMessages(
                getCurrentUser(), targetUser, MAX_MESSAGES)) {
            observeMessage(message);
        }
        final TextField messageField = new TextField();
        messageField.setWidth("100%");
        final Button sendButton = new Button("Send");
        sendButton.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                String messageContent = messageField.getValue();
                if (!messageContent.isEmpty()) {
                    messageField.setValue("");
                    Message message = new Message(getCurrentUser(), targetUser,
                            messageContent);
                    // TODO Send the message

                }
            }
        });
        sendButton.setClickShortcut(KeyCode.ENTER);
        Panel messagePanel = new Panel(messageLayout);
        messagePanel.setHeight("400px");
        messagePanel.setWidth("100%");
        chatLayout.addComponent(messagePanel);
        HorizontalLayout entryLayout = new HorizontalLayout(sendButton,
                messageField);
        entryLayout.setWidth("100%");
        entryLayout.setExpandRatio(messageField, 1);
        entryLayout.setSpacing(true);
        chatLayout.addComponent(entryLayout);
        return chatLayout;
    }

    private Layout buildUserSelectionLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setWidth("100%");
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(new Label("Select user to talk to:"));
        for (User user : userDAO.getUsers()) {
            if (user.equals(getCurrentUser())) {
                continue;
            }
            layout.addComponent(generateUserSelectionButton(user));
        }
        return layout;
    }

    private Button generateBackButton() {
        Button button = new Button("Back");
        button.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getNavigator().navigateTo("chat");
            }
        });
        return button;
    }

    private Button generateUserSelectionButton(final User user) {
        Button button = new Button(user.getName());
        button.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                getUI().getNavigator().navigateTo("chat/" + user.getUsername());
            }
        });
        return button;
    }

    private void observeMessage(Message message) {
        User currentUser = getCurrentUser();
        if (targetUser != null && message.involves(currentUser, targetUser)) {
            if (messageLayout != null) {
                if (messageLayout.getComponentCount() >= MAX_MESSAGES) {
                    messageLayout.removeComponent(messageLayout
                            .getComponentIterator().next());
                }
                messageLayout.addComponent(new Label(message.toString()));
            }
        }
    }

    private User getCurrentUser() {
        // TODO Implement this
        return null;
    }

}