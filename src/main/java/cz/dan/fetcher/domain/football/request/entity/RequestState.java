package cz.dan.fetcher.domain.football.request.entity;

public enum RequestState {
    SCHEDULED {
        @Override
        public RequestState toError() {
            return ERROR;
        }

        @Override
        public RequestState toRetry() {
            return RETRY;
        }

        @Override
        public RequestState toResourceNotFound() {
            return RESOURCE_NOT_FOUND;
        }

        @Override
        public RequestState toCompleted() {
            return COMPLETED;
        }
    },
    RETRY {
        @Override
        public RequestState toError() {
            return ERROR;
        }

        @Override
        public RequestState toResourceNotFound() {
            return RESOURCE_NOT_FOUND;
        }

        @Override
        public RequestState toCompleted() {
            return COMPLETED;
        }
    },
    ERROR,
    RESOURCE_NOT_FOUND,
    COMPLETED;

    public RequestState toRetry() {
        throw new IllegalStateException("Not possible to move FootballPlayerRequest to RETRY.");
    }

    public RequestState toError() {
        throw new IllegalStateException("Not possible to move FootballPlayerRequest to ERROR.");
    }

    public RequestState toResourceNotFound() {
        throw new IllegalStateException("Not possible to move FootballPlayerRequest to RESOURCE_NOT_FOUND.");
    }

    public RequestState toCompleted() {
        throw new IllegalStateException("Not possible to move FootballPlayerRequest to COMPLETED.");
    }
}
