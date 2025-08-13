package api.apiAdminCommerce.to;

import java.util.List;

public class AllRequirementsTo {

    private List<RequirementTo> requirementChargedList;

    private List<RequirementTo> requirementNoChargedList;

    public AllRequirementsTo() {
    }

    public AllRequirementsTo(List<RequirementTo> requirementChargedList, List<RequirementTo> requirementNoChargedList) {
        this.requirementChargedList = requirementChargedList;
        this.requirementNoChargedList = requirementNoChargedList;
    }

    public List<RequirementTo> getRequirementChargedList() {
        return requirementChargedList;
    }

    public void setRequirementChargedList(List<RequirementTo> requirementChargedList) {
        this.requirementChargedList = requirementChargedList;
    }

    public List<RequirementTo> getRequirementNoChargedList() {
        return requirementNoChargedList;
    }

    public void setRequirementNoChargedList(List<RequirementTo> requirementNoChargedList) {
        this.requirementNoChargedList = requirementNoChargedList;
    }
}
