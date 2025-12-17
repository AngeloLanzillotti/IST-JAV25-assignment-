package view.panels;

import model.entity.base.CharacterModel;

/** Interface for classes that need to handle character selection events.
 * <p>
 *     Implementing this interface allows a class to be notified when a {@link CharacterModel}
 *     has been selected, typically from a character or enemy selection UI panel.
 * </p>
 * @see CharacterModel */
public interface CharacterSelectionInterface {
    /** Called when a character is selected.
     * @param selectedCharacter the {@link CharacterModel} that was chosen by the user. */
    void onCharacterSelected(CharacterModel selectedCharacter);
}
