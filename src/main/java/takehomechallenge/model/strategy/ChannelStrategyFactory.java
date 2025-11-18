package takehomechallenge.model.strategy;

import org.springframework.stereotype.Component;
import takehomechallenge.model.ChannelType;

@Component
public class ChannelStrategyFactory {
    // Spring inyecta automáticamente estas estrategias
    // porque todas tienen @Component
    private final EmailStrategy emailStrategy;
    private final SmsStrategy smsStrategy;
    private final PushStrategy pushStrategy;

    /**
     * Constructor - Spring inyecta las estrategias
     */
    public ChannelStrategyFactory(
            EmailStrategy emailStrategy,
            SmsStrategy smsStrategy,
            PushStrategy pushStrategy
    ) {
        this.emailStrategy = emailStrategy;
        this.smsStrategy = smsStrategy;
        this.pushStrategy = pushStrategy;
    }

    /**
     * Obtiene la estrategia correcta según el canal
     *
     * Este es el método clave del Factory
     *
     * Ejemplo de uso:
     * IChannelStrategy strategy = factory.getStrategy(ChannelType.EMAIL);
     * strategy.send(notification); // Ejecuta EmailStrategy.send()
     *
     * @param channel El tipo de canal
     * @return La estrategia correspondiente
     */
    public IChannelStrategy getStrategy(ChannelType channel) {
        // Switch expression (Java 14+)
        // Más limpio y seguro que if/else
        return switch (channel) {
            case EMAIL -> emailStrategy;
            case SMS -> smsStrategy;
            case PUSH -> pushStrategy;
            // Si agregamos un nuevo canal al enum pero olvidamos agregarlo aquí,
            // el compilador nos avisa (exhaustiveness checking)
        };
    }
}
