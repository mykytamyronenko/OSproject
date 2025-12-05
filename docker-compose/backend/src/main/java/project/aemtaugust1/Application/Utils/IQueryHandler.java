package project.aemtaugust1.Application.Utils;

import java.io.FileNotFoundException;

public interface IQueryHandler<I, O> {
    O handle(I input) throws FileNotFoundException;
}
