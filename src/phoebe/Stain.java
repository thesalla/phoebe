package phoebe;

/**
 * A foltokat megvalósító osztályok. Minden folt szerepelhet pontosan egy cellán.
 * Ha egy játékos olyan cellára lép, amelyen folt is van, a folt valamilyen negatív hatást fejt ki a játékosra.
 */
public abstract class Stain implements GameObject {

	/**
	 * Absztrakt metódus az egyes Stain-ek interakcióira.
	 * @param A Player, amelyre a cella hat.
	 */
	public abstract void interact(Player player);

	public abstract String ToString();

}
