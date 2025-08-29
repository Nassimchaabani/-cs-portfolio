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
    abstract fun turn()
}
class SimpleComputerPlayer(name:String) : Player(name) {
    private var stash : List<Card> = emptyList()
    override fun turn() {
        val card1 = Table.reveal(Table.validIndicesToPickFrom().shuffled().first())
        Table.print()
        val card2 = Table.reveal(Table.validIndicesToPickFrom().shuffled().first())
        Table.print()
        if (card1 == card2) {
            Table.takeCards()
        } else {
            Table.hideCards()
        }
    }
}


class CleverComputerPlayer(name:String) : Player(name), TableObserver {

    private var observations = emptyList<Observation>()
    override fun turn() {
        //
    }

    override fun observe(o:Observation) {
        observations += o
        println("I saw: $o")
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
            players[activeId].turn()
        }
    }
}
fun main() {
    val player1 = SimpleComputerPlayer("joe")
    val player2 = CleverComputerPlayer("HAL9000")

    Table.registerObserver(player2)

    val game = Game(arrayOf(player1, player2))

    game.init()
    game.run()
    Table.print()
}
