enum class CardStatus { FACE_UP, FACE_DOWN }
data class Card(val symbol:String, var status: CardStatus = CardStatus.FACE_DOWN) {
    override fun toString(): String {
        return if (this == NO_CARD || status == CardStatus.FACE_UP) symbol else "xxxx"
    }
}

val NO_CARD = Card("____")

data class Observation(val idx:Int, val symbol: String)
interface TableObserver {
  fun observe(o:Observation)
}

abstract class Player(val name:String) {
    // true if picked two cards
    abstract fun turn() : Boolean
}
class SimpleComputerPlayer(name:String) : Player(name) {
    private var stash : List<Card> = emptyList()
    override fun turn() : Boolean {
        println("$name's turn")
        val card1 = Table.reveal(Table.validIndicesToPickFrom().shuffled().first())
        Table.print()
        val card2 = Table.reveal(Table.validIndicesToPickFrom().shuffled().first())
        Table.print()
        if (card1 == card2) {
            Table.takeCards()
            return true
        } else {
            Table.hideCards()
            return false
        }
    }
}

class HumanPlayer(name:String) : Player(name) {
    override fun turn() : Boolean {
        println("$name's turn")
        Table.print()
        val card1 = Table.reveal(readIndex())
        Table.print()
        val card2 = Table.reveal(readIndex())
        if (card1 == card2) {
            Table.takeCards()
            println("yay, two more cards!")
            return true
        } else {
            Table.hideCards()
            println(":(  maybe next time ...")
            return false
        }
    }

    fun readIndex() : Int {
        for (i in 1 .. 3) {
            print("please enter a valid index: ")
            val n : Int? = readln().toIntOrNull()
            if (n != null && n in Table.validIndicesToPickFrom()) {
                return n
            }
        }
        throw Exception("three failed attempts at reading number.")
    }
}

class CleverComputerPlayer(name:String) : Player(name), TableObserver {

    private var observations = emptyList<Observation>()
    override fun turn() : Boolean {
        updateMemory()

        // do I know a symbol with two indices
        for (i in observations.indices) {
            for (j in observations.indices) {
                if (i != j &&
                    observations[i].symbol == observations[j].symbol &&
                    observations[i].idx != observations[j].idx) {
                    val card1 = Table.reveal(observations[i].idx)
                    val card2 = Table.reveal(observations[j].idx)
                    assert(card1 == card2)
                    Table.takeCards()
                    return true
                }
            }
        }

        // reveal random card
        val idx1 = Table.validIndicesToPickFrom().shuffled().first()
        val card1 = Table.reveal(idx1)

        // if I know the second index, I use
        for (i in observations.indices) {
            if (observations[i].symbol == card1.symbol && observations[i].idx != idx1) {
                val card2 = Table.reveal(observations[i].idx)
                assert(card1 == card2)
                Table.takeCards()
                return true
            }
        }

        val card2 = Table.reveal(Table.validIndicesToPickFrom().shuffled().first())
        if (card1 == card2) {
            Table.takeCards()
            return true
        } else {
            Table.hideCards()
            return false
        }

    }

    override fun observe(o:Observation) {
        observations += o
        println("I saw: $o")
    }

    fun updateMemory() {
        val oldObservations = observations
        observations = emptyList()
        for (o in oldObservations) {
            if (o.idx in Table.validIndicesToPickFrom()) {
                observations += o
            }
        }
    }
}

object Table {

    private var observers : List<TableObserver> = emptyList()

    private var cards : Array<Card> = arrayOf(
        Card("Boot"), Card("Boot"),
        Card("Haus"), Card("Haus"),
        Card("Baum"), Card("Baum"),
        )

    fun print() {
        for (i in cards.indices) {
            print("$i: ${cards[i]} ")
            if (i % 3 == 2) println()
        }
        println()
    }
    fun reveal(idx:Int) : Card {
        cards[idx].status = CardStatus.FACE_UP
        notifyObservers(idx, cards[idx])
        return cards[idx]
    }
    fun hideCards() {
        for (card in cards) {
            card.status = CardStatus.FACE_DOWN
        }
    }
    fun takeCards() : List<Card> {
        var handToPlayer = emptyList<Card>()
        for (i in cards.indices) {
            if (cards[i].status == CardStatus.FACE_UP) {
                handToPlayer += cards[i]
                cards[i] = NO_CARD
            }
        }
        return handToPlayer
    }
    fun validIndicesToPickFrom() : List<Int> {
        var valid = emptyList<Int>()
        for (i in cards.indices) {
            if (cards[i] != NO_CARD && cards[i].status == CardStatus.FACE_DOWN) {
                valid += i
            }
        }
        return valid
    }
    fun empty() : Boolean {
        for (card in cards) {
            if (card != NO_CARD) return false
        }
        return true
    }
    fun shuffle() {
        cards.shuffle()
    }

    fun registerObserver(obs:TableObserver) {
        observers += obs
    }

    private fun notifyObservers(idx:Int, card:Card) {
        for (obs in observers) {
            obs.observe( Observation(idx, card.symbol) )
        }
    }
}

class Game(val players: Array<Player>) {

    fun init() {
        Table.shuffle()
    }
    fun run() {
        var activeId = 0
        while (!Table.empty()){
            Table.print()
            val success = players[activeId].turn()
            if (!success) {
                activeId = (activeId+1) % players.size
            }
        }
    }
}
fun main() {
    try {
        val player1 = SimpleComputerPlayer("joe")
        val player2 = CleverComputerPlayer("HAL9000")

        val player3 = HumanPlayer("Alice")

        Table.registerObserver(player2)

        val game = Game(arrayOf(player2, player1))

        game.init()
        game.run()
        Table.print()
    }
    catch (e:Exception) {
        println("an exception occured: ${e.message}")
        // clean up
    }
}
