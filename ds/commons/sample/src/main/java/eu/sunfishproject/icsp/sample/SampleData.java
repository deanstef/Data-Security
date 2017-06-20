/**
 * 
 */
package eu.sunfishproject.icsp.sample;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author <a href="mailto:bernd.pruenster@a-sit.at">Bernd Pr&uuml;nster</a>
 *
 */
@XmlRootElement
public class SampleData {

  private String name;

  private Date   date;

  public SampleData() {
    date = new Date();
  }

  public SampleData(String name) {
    this();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameData() {
    return getName();
  }

  public Date getDate() {
    return date;
  }
}
