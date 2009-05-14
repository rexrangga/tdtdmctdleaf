package draughts.moves;

import java.util.HashMap;
import java.util.Map;

public class LookupTable {

	private Map<Key, Data> lookupTable = new HashMap<Key, Data>();
	private int cacheHit = 0, cacheMiss = 0;

	public Data lookup(Node node, int depth) {
		Data d = lookupTable.get(new Key(node, depth));
		if (d != null) {
			cacheHit++;
		} else {
			cacheMiss++;
		}
		return d;
	}

	public void storeUpper(Node node, int depth, double upper) {
		Key key = new Key(node, depth);
		Data data = lookupTable.get(key);
		if (data == null) {
			data = new Data();
			lookupTable.put(key, data);
		}
		data.setUpper(upper);
	}

	public void storeLower(Node node, int depth, double lower) {
		Key key = new Key(node, depth);
		Data data = lookupTable.get(key);
		if (data == null) {
			data = new Data();
			lookupTable.put(key, data);
		}
		data.setLower(lower);
	}

	public class Data {
		private Double lower, upper;

		public Double getLower() {
			return lower;
		}

		public void setLower(Double lower) {
			this.lower = lower;
		}

		public Double getUpper() {
			return upper;
		}

		public void setUpper(Double upper) {
			this.upper = upper;
		}
	}
}

class Key {

	private final Node node;

	// private final int depth;

	public Key(Node node, int depth) {
		this.node = new Node(node.getBoard());
		// this.depth = depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// result = prime * result + depth;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		// if (depth != other.depth)
		// return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

}
