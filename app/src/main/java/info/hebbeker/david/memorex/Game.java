package info.hebbeker.david.memorex;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

interface Symbol
{

}

interface GameBoardInterface
{
    /**
     * Signal a sequence of availableSymbols to the user.
     * <p>
     * While signalling the symbols must be locked / disabled.
     * This action is queued to the UIs thread and run asynchronously.
     *
     * @param symbolSequence is the sequence of symbols to be signalled to the player
     */
    void queueDisplaySymbolSequence(final ArrayList<Symbol> symbolSequence);

    /**
     * Notify the user about a game related event.
     *
     * @param userMessage is the message which is displayed to the user
     */
    void notifyUser(final String userMessage);

    /**
     * Set game board to a state where the player sees all symbols.
     */
    void deployGameBoard();

    /**
     * Put away all graphical game elements which are necessary only for playing.
     */
    void clearBoard();
}

final class Game
{
    private final GameBoardInterface gameBoardInterface;
    private final Symbol[] availableSymbols;
    private final ArrayList<Symbol> symbolSequence = new ArrayList<>();
    private final Random random = new Random();
    private ListIterator<Symbol> currentSymbol;

    Game(final GameBoardInterface gameBoardInterface, Symbol[] availableSymbols)
    {
        this.gameBoardInterface = gameBoardInterface;
        this.availableSymbols = availableSymbols;
    }

    void startNewGame()
    {
        symbolSequence.clear();
        gameBoardInterface.deployGameBoard();
        nextLevel();
    }

    private void nextLevel()
    {
        symbolSequence.add(availableSymbols[random.nextInt(availableSymbols.length)]);
        currentSymbol = symbolSequence.listIterator();
        gameBoardInterface.notifyUser("Level " + symbolSequence.size());
        gameBoardInterface.queueDisplaySymbolSequence(symbolSequence);
    }

    void putPlayerInput(final Symbol symbol, final HighScoreContainer highScoreContainer)
    {
        // if input symbol is not correct, notify user and reset board game
        if (symbol != currentSymbol.next())
        {
            // end current game
            final int completedLevel = symbolSequence.size() - 1;
            final Score score = new Score(completedLevel);
            highScoreContainer.setNewHighScore(score);
            gameBoardInterface.notifyUser("Game Over (" + completedLevel + ")");
            gameBoardInterface.clearBoard();
        }
        // if sequence is complete, notify user and start next level
        else if (!currentSymbol.hasNext())
        {
            nextLevel();
        }
    }
}