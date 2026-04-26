export interface ValueRange {
  min: number
  average: number
  max: number
}

export interface Attribute {
  name: string
  description: string
  value: number
}

export interface Skill {
  name: string
  linkedAttributeName: string
  level: number
}

export interface RulesetData {
  valueRange: ValueRange
  attributes: Attribute[]
  skills: Skill[]
}

export interface RollResult {
  dice: number[]
  success: boolean
  paschValue: number | null
  paschCount: number | null
}

const BASE = '/api/rulesets'

async function checkOk(res: Response, msg: string): Promise<void> {
  if (!res.ok) {
    const body = await res.text().catch(() => '')
    throw new Error(`${msg} (${res.status}${body ? ': ' + body : ''})`)
  }
}

export async function importRuleset(xml: string): Promise<RulesetData> {
  const res = await fetch(`${BASE}/import`, {
    method: 'POST',
    headers: { 'Content-Type': 'text/xml' },
    body: xml,
  })
  await checkOk(res, 'XML konnte nicht geladen werden')
  return res.json()
}

export async function saveRuleset(name: string, data: RulesetData): Promise<void> {
  const res = await fetch(`${BASE}/${encodeURIComponent(name)}`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  await checkOk(res, 'Regelwerk konnte nicht gespeichert werden')
}

export async function rollDice(value: number): Promise<RollResult> {
  const res = await fetch('/api/roll', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ value }),
  })
  await checkOk(res, 'Würfeln fehlgeschlagen')
  return res.json()
}

export async function exportRuleset(data: RulesetData): Promise<string> {
  const res = await fetch(`${BASE}/export`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  })
  await checkOk(res, 'XML konnte nicht serialisiert werden')
  return res.text()
}
