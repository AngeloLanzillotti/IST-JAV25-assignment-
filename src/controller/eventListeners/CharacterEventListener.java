package controller.eventListeners;

import model.entity.base.CharacterModel;

/** Interface defining callbacks for key events related to a {@link CharacterModel}.
 * <p>
 *     This listener is primarily used by the Model ({@code CharacterModel}) to notify the Controller and View
 *     about actions performed, allowing the UI to update or specific logic to be executed.
 * </p>
 * @see CharacterModel
 */
public interface CharacterEventListener {
    /** Called when the character initiates or suffers an attack.
     * This event is typically used to trigger visual updates, play sounds, or log combat results.
     * @param character The {@link CharacterModel} instance involved in the attack (either the attacker or the target).
     * @param message   A descriptive string providing details about the attack. */
    void onAttack(CharacterModel character, String message);
}
