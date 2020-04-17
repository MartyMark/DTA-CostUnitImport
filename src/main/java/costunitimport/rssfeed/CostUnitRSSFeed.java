package costunitimport.rssfeed;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class CostUnitRSSFeed {

	private CostUnitRSSChannel channel;

	public CostUnitRSSChannel getChannel() {
		return channel;
	}

	@XmlElement(name = "channel")
	public void setChannel(CostUnitRSSChannel channel) {
		this.channel = channel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("CostUnitRSSFeed[");
		builder.append("channel:").append(getChannel());
		builder.append("]");
		return builder.toString();
	}
}
