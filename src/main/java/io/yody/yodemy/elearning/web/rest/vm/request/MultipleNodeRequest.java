package io.yody.yodemy.elearning.web.rest.vm.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.Valid;

public class MultipleNodeRequest {

    @Valid
    @JsonProperty("nodes_request")
    List<NodeRequest> nodesrequest;

    @JsonProperty("edges_request")
    List<EdgeRequest> edgesRequest;

    public List<NodeRequest> getNodesrequest() {
        return nodesrequest;
    }

    public void setNodesrequest(List<NodeRequest> nodesrequest) {
        this.nodesrequest = nodesrequest;
    }

    public List<EdgeRequest> getEdgesRequest() {
        return edgesRequest;
    }

    public void setEdgesRequest(List<EdgeRequest> edgesRequest) {
        this.edgesRequest = edgesRequest;
    }
}
