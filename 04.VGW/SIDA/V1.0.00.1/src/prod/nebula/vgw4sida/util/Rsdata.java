package prod.nebula.vgw4sida.util;

public class Rsdata
{

    public Rsdata()
    {
        name = "";
        type = "";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }

    private String name;
    private String type;
}
