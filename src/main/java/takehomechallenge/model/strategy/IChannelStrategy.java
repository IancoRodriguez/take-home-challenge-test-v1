package takehomechallenge.model.strategy;

import takehomechallenge.model.Notification;

public interface IChannelStrategy {
    void send(Notification n);
}
