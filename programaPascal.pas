program completo;
var
    x, y: integer;
begin
    writeln('------------- READ --------------');
    read(x);
    read(y);
    {read(x, y);}

    writeln('------------- WRITE --------------');
    writeln(x);
    writeln('texto');
    writeln(10);
    writeln(x);
    writeln('texto');
    writeln(10);

    writeln('------------- FOR 1 --------------');
    for x := 1 to 10 do begin
        writeln(x);
        writeln(y);
    end;

    writeln('------------- REPEAT --------------');
    x := 1;
    repeat
        writeln(x);
        x := x + 1;
    until (x > 10);

    writeln('------------- WHILE --------------');
    while (x >= 1) do begin
        writeln(x);
        x := x - 1;
    end;

    writeln('------------- IF 1 --------------');
    if (x >= 10) then
    begin
        writeln('ENTROU NO IF 1');
        writeln(x);
        writeln(y);
    end;

    writeln('------------- IF ELSE 2 --------------');
    if (x >= 10) then
    begin
        writeln('ENTROU NO IF 2');
        writeln(x);
        writeln(y);
    end
    else
    begin
        writeln('ENTROU NO ELSE 2');
        writeln(y);
        writeln(x);
    end;

    writeln('------------- IF 3 --------------');
    if (x >= 10 and y <= 10) then
    begin
        writeln('ENTROU NO IF 3');
        writeln(x);
        writeln(y);
    end;

    writeln('------------- IF 4 --------------');
    if (x >= 10 or y <= 10) then
    begin
        writeln('ENTROU NO IF 4');
        writeln(x);
        writeln(y);
    end;

    writeln('------------- IF 5 --------------');
    if (not (x >= 10)) then
    begin
        writeln('ENTROU NO IF 5');
        writeln(x);
        writeln(y);
    end;

    writeln('------------- ATRIBUICAO --------------');
    x := 10;
    writeln('x:=10');
    writeln(x);
end.