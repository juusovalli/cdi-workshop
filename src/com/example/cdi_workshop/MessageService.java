package com.example.cdi_workshop;

import java.util.List;

import com.vaadin.ui.UI;

public interface MessageService {

    public List<Message> getLatestMessages(User user1, User user2, int n);

    public void registerParticipant(User user, UI ui);

    public void unregisterParticipant(User user);

}
